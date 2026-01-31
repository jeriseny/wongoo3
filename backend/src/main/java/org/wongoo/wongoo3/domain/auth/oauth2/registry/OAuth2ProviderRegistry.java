package org.wongoo.wongoo3.domain.auth.oauth2.registry;

import org.springframework.stereotype.Component;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.oauth2.provider.OAuth2Provider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OAuth2ProviderRegistry {

    private final Map<ProviderType, OAuth2Provider> providers;

    public OAuth2ProviderRegistry(List<OAuth2Provider> providerList) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(OAuth2Provider::getProviderType, provider -> provider));
    }

    public OAuth2Provider getProvider(ProviderType providerType) {
        return providers.get(providerType);
    }
}
