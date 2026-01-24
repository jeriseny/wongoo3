package org.wongoo.wongoo3.global.jwt.token;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wongoo.wongoo3.domain.login.dto.UserRole;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    @Getter
    private final Long accessTokenExpirationMillis;
    @Getter
    private final Long refreshTokenExpirationMillis;

    public JwtProvider(SecretKey secretKey,
                       @Value("${jwt.access-expiration-ms}") Long accessTokenExpirationMillis,
                       @Value("${jwt.refresh-expiration-ms}") Long refreshTokenExpirationMillis) {
        this.secretKey = secretKey;
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
    }

    public String createAccessToken(Long userId, String email, UserRole role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMillis);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId, boolean rememberMe) {
        long expireTime = rememberMe
                ? refreshTokenExpirationMillis             // 14일
                : Duration.ofDays(1).toMillis();           // 1일

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }
}
