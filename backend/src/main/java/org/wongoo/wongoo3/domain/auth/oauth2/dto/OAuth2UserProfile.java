package org.wongoo.wongoo3.domain.auth.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserProfile {
    String providerId;
    String email;
    String nickname;
    String phoneNumber;
    String profileImageUrl;
}
