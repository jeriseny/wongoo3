package org.wongoo.wongoo3.domain.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderLoginResponse;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.oauth2.registry.OAuth2ProviderRegistry;
import org.wongoo.wongoo3.domain.auth.service.AuthService;
import org.wongoo.wongoo3.domain.token.dto.WkToken;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.service.UserService;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final AuthService authService;
    private final UserService userService;
    private final OAuth2ProviderRegistry oAuth2ProviderRegistry;

    @Transactional
    public String getLoginUri(ProviderType providerType, String state) {
        return oAuth2ProviderRegistry.getProvider(providerType).buildLoginUri(state);
    }

    @Transactional
    public OAuth2UserProfile getUserProfile(ProviderType providerType, String authorizationCode, String state) {
        return oAuth2ProviderRegistry.getProvider(providerType).getUserProfile(authorizationCode, state);
    }

    @Transactional
    public ProviderLoginResponse processOAuth2Login(OAuth2UserProfile userProfile, ProviderType providerType, boolean rememberMe) {
        User userByProvider = userService.getUserByProviderId(userProfile.getProviderId());

        ProviderLoginResponse response = new ProviderLoginResponse();

        if (userByProvider != null) {
            // TODO 비밀번호 null 처리 로직 개선 필요
            WkToken wkToken = authService.login(userByProvider.getEmail(), null, rememberMe);
            response.setWkToken(wkToken);
            response.setNewUser(false);
            return response;
        }

        User userByEmail = userService.getUserByEmail(userProfile.getEmail());
        if (userByEmail != null) {
            userByEmail.setProviderId(userProfile.getProviderId());
            userByEmail.setProviderType(providerType);
            WkToken wkToken = authService.login(userByEmail.getEmail(), null, rememberMe);
            response.setWkToken(wkToken);
            response.setNewUser(false);
            return response;
        }

        response.setUserProfile(userProfile);
        response.setNewUser(true);
        return response;
    }
}
