package org.wongoo.wongoo3.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;
import org.wongoo.wongoo3.domain.terms.dto.TermsType;
import org.wongoo.wongoo3.domain.token.RefreshToken;
import org.wongoo.wongoo3.domain.token.dto.WkToken;
import org.wongoo.wongoo3.domain.token.repository.RefreshTokenRepository;
import org.wongoo.wongoo3.domain.token.service.TokenService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.dto.SocialSignUpRequest;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;
import org.wongoo.wongoo3.global.jwt.token.JwtParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtParser jwtParser;
    private final UserRepository userRepository;

    @Transactional
    public WkToken login(String email, String password, boolean rememberMe) {
        Optional<User> userO = userService.getUserByEmail(email);
        if (userO.isEmpty()) {
            throw new WebErrorException(WebErrorCode.UNAUTHORIZED, "존재하지 않는 계정입니다");
        }
        User user = userO.get();
        if (user.getPassword() == null) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "소셜 로그인으로 가입한 사용자 입니다");
        }

        if (!ObjectUtils.isEmpty(password)) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new WebErrorException(WebErrorCode.UNAUTHORIZED, "비밀번호가 일치하지 않습니다");
            }
        }

        WkToken wkToken = tokenService.issueTokens(user.getId(), user.getEmail(), user.getRole(), rememberMe);
        user.setLastLoginAt(LocalDateTime.now());
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken(user, wkToken.getRefreshToken(), LocalDateTime.now().plus(wkToken.getRefreshTokenExpiresIn(), ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshToken);

        return wkToken;
    }

    @Transactional
    public WkToken processOAuth2Login(OAuth2UserProfile userProfile, ProviderType providerType, boolean rememberMe) {
        User userByProvider = userService.getUserByProviderId(userProfile.getProviderId());

        if (userByProvider != null) {
            return tokenService.issueTokens(userByProvider.getId(), userByProvider.getEmail(), userByProvider.getRole(), rememberMe);
        }

        Optional<User> userByEmailO = userService.getUserByEmail(userProfile.getEmail());
        if (userByEmailO.isPresent()) {
            User userByEmail = userByEmailO.get();
            userByEmail.setProviderId(userProfile.getProviderId());
            userByEmail.setProviderType(providerType);
            return tokenService.issueTokens(userByEmail.getId(), userByEmail.getEmail(), userByEmail.getRole(), rememberMe);
        }
        SocialSignUpRequest signUpRequest = new SocialSignUpRequest(
                userProfile.getEmail(),
                userProfile.getNickname(),
                userProfile.getPhoneNumber(),
                userProfile.getProfileImageUrl(),
                providerType,
                userProfile.getProviderId(),
                List.of(new TermsRequest(TermsType.SERVICE, true), new TermsRequest(TermsType.PRIVACY, true),
                        new TermsRequest(TermsType.MARKETING, false), new TermsRequest(TermsType.KAKAO_AD, false))
        );
        Long newUserId = userService.signUp(signUpRequest);
        User user = userRepository.findById(newUserId)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "가입된 사용자를 찾을 수 없습니다: " + newUserId));
        user.setLastLoginAt(LocalDateTime.now());

        WkToken wkToken = tokenService.issueTokens(user.getId(), user.getEmail(), user.getRole(), rememberMe);
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken(user, wkToken.getRefreshToken(), LocalDateTime.now().plus(wkToken.getRefreshTokenExpiresIn(), ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshToken);

        return wkToken;
    }

    @Transactional
    public WkToken reissueTokens(String refreshToken, boolean rememberMe) {
        Claims claims = jwtParser.parseToken(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());

        User user = userService.getUserById(userId);

        RefreshToken savedToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다"));

        if (savedToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new WebErrorException(WebErrorCode.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다");
        }

        refreshTokenRepository.deleteByUser(user);

        WkToken newToken = tokenService.issueTokens(userId, user.getEmail(), user.getRole(), rememberMe);

        RefreshToken newRefreshToken = new RefreshToken(user, newToken.getRefreshToken(), LocalDateTime.now().plus(newToken.getRefreshTokenExpiresIn(), ChronoUnit.MILLIS));
        refreshTokenRepository.save(newRefreshToken);

        return newToken;
    }
}
