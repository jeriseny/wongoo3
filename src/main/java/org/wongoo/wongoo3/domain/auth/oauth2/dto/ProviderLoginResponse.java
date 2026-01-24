package org.wongoo.wongoo3.domain.auth.oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import org.wongoo.wongoo3.domain.token.dto.WkToken;

@Getter
@Setter
public class ProviderLoginResponse {
    boolean isNewUser;
    private OAuth2UserProfile userProfile;
    private WkToken wkToken;
}
