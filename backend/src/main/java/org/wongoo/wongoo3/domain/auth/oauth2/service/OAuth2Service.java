package org.wongoo.wongoo3.domain.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.oauth2.registry.OAuth2ProviderRegistry;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final OAuth2ProviderRegistry oAuth2ProviderRegistry;

    @Transactional
    public String getLoginUri(ProviderType providerType, String state) {
        return oAuth2ProviderRegistry.getProvider(providerType).buildLoginUri(state);
    }

    @Transactional
    public OAuth2UserProfile getUserProfile(ProviderType providerType, String authorizationCode, String state) {
        return oAuth2ProviderRegistry.getProvider(providerType).getUserProfile(authorizationCode, state);
    }
}
