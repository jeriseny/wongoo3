package org.wongoo.wongoo3.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.service.AuthService;
import org.wongoo.wongoo3.domain.token.dto.WkToken;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public OAuth2SuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOAuth2User customOAuth2User) {
            OAuth2UserProfile profile = customOAuth2User.getProfile();
            ProviderType providerType = customOAuth2User.getProviderType();
            WkToken wkToken = authService.processOAuth2Login(profile, providerType, false);

            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth/callback")
                    .queryParam("accessToken", wkToken.getAccessToken())
                    .queryParam("refreshToken", wkToken.getRefreshToken())
                    .build().toUriString();

            response.sendRedirect(redirectUrl);
        } else {
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth/callback")
                    .queryParam("error", "OAuth2 로그인 실패")
                    .build().toUriString();
            response.sendRedirect(errorUrl);
        }
    }
}
