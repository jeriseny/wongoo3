package org.wongoo.wongoo3.domain.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.token.dto.WkToken;
import org.wongoo.wongoo3.global.jwt.token.JwtProvider;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;

    public WkToken issueTokens(Long userId, String email, UserRole role, boolean rememberMe) {
        String accessToken = jwtProvider.createAccessToken(userId, email, role);
        String refreshToken = jwtProvider.createRefreshToken(userId, rememberMe);

        return new WkToken(accessToken, refreshToken, jwtProvider.getAccessTokenExpirationMillis(), jwtProvider.getRefreshTokenExpirationMillis());
    }
}
