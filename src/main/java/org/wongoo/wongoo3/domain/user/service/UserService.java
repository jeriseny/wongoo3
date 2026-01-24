package org.wongoo.wongoo3.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.terms.userterms.service.UserTermsService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.factory.UserSignUpFactory;
import org.wongoo.wongoo3.domain.user.dto.SignUpRequest;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserTermsService userTermsService;
    private final UserSignUpFactory userSignUpFactory;
    private final UserRepository userRepository;

    @Transactional
    public Long signUp(SignUpRequest request) {
        validateDuplicateUser(request.email(), request.nickname(), request.phoneNumber());

        User user = userSignUpFactory.create(request);
        userRepository.save(user);

        userTermsService.addUserTerms(user, request.termsAgreeList());
        return user.getId();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다: " + email));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    }

    public User getUserByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }


    @Transactional
    public void changeUserRole(Long userId, UserRole role, Long loginUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + userId));
        if (userId.equals(loginUserId)) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "본인의 권한은 변경할 수 없습니다");
        }
        user.changeRole(role);
    }

    private void validateDuplicateUser(String email, String nickname, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new WebErrorException(WebErrorCode.CONFLICT, "이미 사용 중인 이메일입니다");
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new WebErrorException(WebErrorCode.CONFLICT, "이미 사용 중인 닉네임입니다");
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new WebErrorException(WebErrorCode.CONFLICT, "이미 사용 중인 휴대폰 번호입니다");
        }
    }
}
