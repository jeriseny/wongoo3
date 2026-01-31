package org.wongoo.wongoo3.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.terms.dto.AddTermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.terms.service.TermsService;
import org.wongoo.wongoo3.domain.terms.userterms.UserTerms;
import org.wongoo.wongoo3.domain.terms.userterms.service.UserTermsService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.dto.LocalSignUpRequest;
import org.wongoo.wongoo3.domain.user.dto.UserInfoRequest;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    LocalSignUpRequest request;
    List<AddTermsRequest> termsRequests;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TermsService termsService;
    @Autowired
    UserTermsService userTermsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        request = new LocalSignUpRequest(
                "aeternus0519@naver.com",
                "자바의제왕",
                "010-2446-9149",
                "15730254791!a",
                List.of(new TermsRequest(TermsType.SERVICE, true),
                        new TermsRequest(TermsType.PRIVACY, true),
                        new TermsRequest(TermsType.MARKETING, true),
                        new TermsRequest(TermsType.KAKAO_AD, false))

        );

        termsRequests = List.of(
                new AddTermsRequest(TermsType.SERVICE, "서비스 약관 내용", true, true),
                new AddTermsRequest(TermsType.PRIVACY, "개인정보 처리방침 내용", true, true),
                new AddTermsRequest(TermsType.MARKETING, "마케팅 활용 동의 내용", false, true),
                new AddTermsRequest(TermsType.KAKAO_AD, "카카오 광고 동의 내용", false, true)
        );
        termsService.addTerms(termsRequests);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void signUp() {
        Long id = userService.signUp(request);
        userRepository.findById(id).ifPresent(user -> {
            assertEquals(request.email(), user.getEmail());
            assertEquals(request.nickname(), user.getNickname());
            assertEquals(request.phoneNumber(), user.getPhoneNumber());
        });
    }

    @Test
    @DisplayName("회원 약관 동의 기록 테스트")
    void signUpUserTerms() {
        Long id = userService.signUp(request);
        Optional<User> optionalUser = userRepository.findById(id);
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        List<UserTerms> userTermsList = userTermsService.getUserTermsByUser(user);
        assertEquals(4, userTermsList.size());

        for (TermsRequest termsRequest : request.termsAgreeList()) {
            TermsType type = termsRequest.termsType();
            boolean isAgreed = termsRequest.agreed();

            Optional<UserTerms> optionalUserTerms = userTermsList.stream()
                    .filter(ut -> ut.getTerms().getType() == type)
                    .findFirst();

            assertTrue(optionalUserTerms.isPresent());
            UserTerms userTerms = optionalUserTerms.get();
            assertEquals(isAgreed, userTerms.isAgreed());
        }
    }

    @Test
    @DisplayName("내 정보 조회 테스트")
    void getUserInfo() {
        Long id = userService.signUp(request);
        var userInfo = userService.getUserInfo(id);

        assertEquals(request.email(), userInfo.email());
        assertEquals(request.nickname(), userInfo.nickName());
        assertEquals(request.phoneNumber(), userInfo.phoneNumber());
    }

    @Test
    @DisplayName("내 정보 변경 테스트")
    void changeInfo() {
        Long id = userService.signUp(request);
        String newNickname = "변경된닉네임";
        String newPhoneNumber = "010-9999-8888";
        UserInfoRequest userInfoRequest = new UserInfoRequest(newNickname, newPhoneNumber, null);
        userService.changeInfo(id, userInfoRequest.nickname, userInfoRequest.phoneNumber);
        User user = userRepository.findById(id).orElseThrow();
        assertEquals(newNickname, user.getNickname());
        assertEquals(newPhoneNumber, user.getPhoneNumber());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePassword() {
        Long id = userService.signUp(request);
        String newPassword = "NewPassword123!";
        userService.changePassword(id, request.password(), newPassword);
        User user = userRepository.findById(id).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
    }

}