package org.wongoo.wongoo3.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.wongoo.wongoo3.domain.token.RefreshToken;
import org.wongoo.wongoo3.domain.token.dto.WkToken;
import org.wongoo.wongoo3.domain.token.repository.RefreshTokenRepository;
import org.wongoo.wongoo3.domain.token.service.TokenService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;
import org.wongoo.wongoo3.global.jwt.token.JwtParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtParser jwtParser;

    @Transactional
    public WkToken login(String email, String password, boolean rememberMe) {
        User user = userService.getUserByEmail(email);

        // TODO 비밀번호 null 처리 로직 개선 필요
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

        RefreshToken refreshToken = new RefreshToken(user, wkToken.getAccessToken(), LocalDateTime.now().plus(wkToken.getRefreshTokenExpiresIn(), ChronoUnit.MILLIS));
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
