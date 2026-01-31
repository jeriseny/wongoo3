package org.wongoo.wongoo3.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient oAuthRestClient() {
        return RestClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultStatusHandler(
                        HttpStatusCode::isError, (req, res) -> {
                            String body = new String(res.getBody().readAllBytes());
                            throw new WebErrorException(WebErrorCode.OAUTH_PROVIDER_ERROR, "OAuth provider error: " + body);
                        }
                )
                .build();
    }
}
