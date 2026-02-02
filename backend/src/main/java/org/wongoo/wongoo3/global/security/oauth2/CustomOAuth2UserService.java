package org.wongoo.wongoo3.global.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());

        OAuth2UserProfile profile = extractUserProfile(providerType, oAuth2User.getAttributes());

        return new CustomOAuth2User(profile, oAuth2User.getAttributes(), providerType);
    }

    private OAuth2UserProfile extractUserProfile(ProviderType providerType, Map<String, Object> attributes) {
        if (providerType == ProviderType.NAVER) {
            return extractNaverProfile(attributes);
        }
        throw new IllegalArgumentException("Unsupported provider type: " + providerType);
    }

    private OAuth2UserProfile extractNaverProfile(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            throw new IllegalArgumentException("Invalid Naver user info response");
        }
        OAuth2UserProfile profile = new OAuth2UserProfile();
        profile.setProviderId((String) response.get("id"));
        profile.setEmail((String) response.get("email"));
        profile.setNickname((String) response.get("nickname"));
        profile.setPhoneNumber((String) response.get("mobile"));
        if (response.get("profile_image") != null) {
            profile.setProfileImageUrl((String) response.get("profile_image"));
        }
        return profile;
    }
}
