package org.wongoo.wongoo3.global.security.oauth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserProfile profile;
    private final ProviderType providerType;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(OAuth2UserProfile profile, Map<String, Object> attributes, ProviderType providerType) {
        this.profile = profile;
        this.attributes = attributes;
        this.providerType = providerType;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return profile.getNickname();
    }
}
