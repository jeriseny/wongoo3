package org.wongoo.wongoo3.domain.auth.oauth2.provider.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.OAuth2UserProfile;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.auth.oauth2.provider.OAuth2Provider;

@Service
@RequiredArgsConstructor
public class NaverOAuth2Provider implements OAuth2Provider {

    private final NaverOauth2Client naverOauth2Client;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.NAVER;
    }

    @Override
    public OAuth2UserProfile getUserProfile(String authorizationCode, String state) {
        NaverToken token = naverOauth2Client.getToken(authorizationCode, state);
        NaverProfile naverProfile = naverOauth2Client.requestUserInfo(token.getAccessToken());
        NaverProfile.response response = naverProfile.getResponse();

        OAuth2UserProfile userProfile = new OAuth2UserProfile();
        userProfile.setProviderId(response.getId());
        userProfile.setEmail(response.getEmail());
        userProfile.setNickname(response.getNickname());
        userProfile.setPhoneNumber(response.getMobile());
        if (!ObjectUtils.isEmpty(userProfile.getProfileImageUrl())) {
            userProfile.setProfileImageUrl(response.getProfile_image());
        }
        return userProfile;
    }

    @Override
    public String buildLoginUri(String state) {
        return naverOauth2Client.buildLoginUri(state);
    }
}
