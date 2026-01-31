package org.wongoo.wongoo3.domain.auth.oauth2.provider;

import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;

public interface OAuth2Provider {

    ProviderType getProviderType();

    OAuth2UserProfile getUserProfile(String authorizationCode, String state);

    String buildLoginUri(String state);
}
