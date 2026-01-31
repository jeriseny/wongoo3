package org.wongoo.wongoo3.global.jwt.token;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;
import org.wongoo.wongoo3.domain.auth.dto.UserRole;

@Component
public class JwtClaimsResolver {

    public Long resolverUserId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }

    public String resolverEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    public LoginUser resolverLoginUser(Claims claims) {
        Long userId = resolverUserId(claims);
        String email = resolverEmail(claims);
        UserRole userRole = UserRole.valueOf(claims.get("role", String.class));
        return new LoginUser(userId, email, userRole);
    }
}
