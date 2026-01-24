package org.wongoo.wongoo3.domain.auth.oauth2.provider.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverOauth2Client {

    private final RestClient restClient;

    @Value("${naver.oauth.client-id}")
    private String clientId;

    @Value("${naver.oauth.redirect-uri}")
    private String redirectUri;

    @Value("${naver.oauth.auth-uri}")
    private String authorizeUri;

    @Value("${naver.oauth.client-secret}")
    private String clientSecret;

    @Value("${naver.oauth.token-uri}")
    private String tokenUri;

    @Value("${naver.oauth.user-info-uri}")
    private String userInfoUri;


    public NaverToken getToken(String authorizationCode, String state) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", authorizationCode);
        form.add("state", state);

        return restClient.post()
                .uri(tokenUri)
                .body(form)
                .retrieve()
                .body(NaverToken.class);
    }

    public NaverProfile requestUserInfo(String accessToken) {
        return restClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(NaverProfile.class);
    }

    public String buildLoginUri(String state) {
        return UriComponentsBuilder
                .fromUriString(authorizeUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .build()
                .toUriString();
    }
}
