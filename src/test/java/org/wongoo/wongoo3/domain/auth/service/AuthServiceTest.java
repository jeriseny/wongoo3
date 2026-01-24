package org.wongoo.wongoo3.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.auth.dto.LoginRequest;
import org.wongoo.wongoo3.domain.terms.dto.AddTermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.terms.service.TermsService;
import org.wongoo.wongoo3.domain.token.dto.WkToken;
import org.wongoo.wongoo3.domain.token.repository.RefreshTokenRepository;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.dto.LocalSignUpRequest;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.jwt.token.JwtClaimsResolver;
import org.wongoo.wongoo3.global.jwt.token.JwtParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;
    @Autowired
    JwtParser jwtParser;
    @Autowired
    JwtClaimsResolver jwtClaimsResolver;

    LocalSignUpRequest signUpRequest;
    LoginRequest loginRequest;
    Long userId;
    List<AddTermsRequest> termsRequests;
    User user;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    TermsService termsService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @BeforeEach
    void setUp() {
        signUpRequest = new LocalSignUpRequest(
                "aeternus0519@naver.com",
                "자바의제왕",
                "010-2446-9149",
                "15730254791!a",
                List.of(new TermsRequest(TermsType.SERVICE, true),
                        new TermsRequest(TermsType.PRIVACY, true),
                        new TermsRequest(TermsType.MARKETING, true),
                        new TermsRequest(TermsType.KAKAO_AD, false))

        );
        loginRequest = new LoginRequest(
                "aeternus0519@naver.com",
                "15730254791!a",
                true
        );
        termsRequests = List.of(
                new AddTermsRequest(TermsType.SERVICE, "서비스 약관 내용", true, true),
                new AddTermsRequest(TermsType.PRIVACY, "개인정보 처리방침 내용", true, true),
                new AddTermsRequest(TermsType.MARKETING, "마케팅 활용 동의 내용", false, true),
                new AddTermsRequest(TermsType.KAKAO_AD, "카카오 광고 동의 내용", false, true)
        );
        termsService.addTerms(termsRequests);
        userId = userService.signUp(signUpRequest);
        user = userRepository.findById(userId).orElseThrow(() -> new AssertionError("User not found"));
    }

    @Test
    @DisplayName("토큰 발급 테스트")
    void testTokenIssuance() {
        WkToken wkToken = authService.loginWithLocal(loginRequest.email(), loginRequest.password(), loginRequest.rememberMe());
        Claims claims = jwtParser.parseToken(wkToken.getAccessToken());

        LoginUser loginUser = jwtClaimsResolver.resolverLoginUser(claims);

        assertEquals(loginRequest.email(), loginUser.email());
        assertEquals(userId, loginUser.userId());
        assertEquals(user.getRole(), loginUser.role());
    }

    @Test
    @DisplayName("Refresh Token으로 토큰 재발급 성공")
    void reissueToken_success() {

        // given: 로그인
        WkToken issuedToken = authService.loginWithLocal(
                loginRequest.email(),
                loginRequest.password(),
                true
        );

        String oldRefreshToken = issuedToken.getRefreshToken();

        // when: 재발급
        WkToken reissuedToken = authService.reissueTokens(oldRefreshToken, true);
        log.info("issuedToken.AccessToken={}, issuedToken.RefreshToken={}", issuedToken.getAccessToken(), issuedToken.getRefreshToken());
        log.info("reissuedToken.AccessToken={}, reissuedToken.RefreshToken={}", reissuedToken.getAccessToken(), reissuedToken.getRefreshToken());
        // then: 토큰이 새로 발급됨
        assertNotNull(reissuedToken.getAccessToken());
        assertNotNull(reissuedToken.getRefreshToken());

        assertNotEquals(
                issuedToken.getAccessToken(),
                reissuedToken.getAccessToken()
        );

        assertNotEquals(
                issuedToken.getRefreshToken(),
                reissuedToken.getRefreshToken()
        );

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow();
        assertTrue(refreshTokenRepository.findByUser(user).isPresent());
    }
}