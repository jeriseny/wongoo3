package org.wongoo.wongoo3.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.terms.userterms.service.UserTermsService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.dto.UserInfoResponse;
import org.wongoo.wongoo3.domain.user.factory.UserSignUpFactory;
import org.wongoo.wongoo3.domain.user.dto.SignUpRequest;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
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

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = getUserById(userId);
        return UserInfoResponse.from(user);
    }

    @Transactional
    public void changeUserRole(Long userId, UserRole role, Long loginUserId) {
        User user = getUserById(userId);
        if (userId.equals(loginUserId)) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "본인의 권한은 변경할 수 없습니다");
        }
        user.changeRole(role);
    }

    @Transactional
    public void changeInfo(Long userId, String nickname, String phoneNumber) {
        User user = getUserById(userId);

        if (!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new WebErrorException(WebErrorCode.CONFLICT, "이미 사용 중인 닉네임입니다");
        }
        if (!user.getPhoneNumber().equals(phoneNumber) && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new WebErrorException(WebErrorCode.CONFLICT, "이미 사용 중인 휴대폰 번호입니다");
        }
        user.changeInfo(nickname, phoneNumber);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new WebErrorException(WebErrorCode.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다");
        }
        if (currentPassword.equals(newPassword)) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다");
        }
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(newEncodedPassword);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    }

    public User getUserByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
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
