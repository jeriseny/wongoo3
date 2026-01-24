package org.wongoo.wongoo3.domain.auth.oauth2.provider.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverProfile {

    @JsonProperty("resultcode")
    private String resultCode;

    private String message;

    private response response;

    @Getter
    static class response {
        private String id;
        private String email;
        private String name;
        private String age;
        private String gender;
        private String birthday;
        @JsonProperty("birthyear")
        private String birthyear;
        private String mobile;

        private String nickname;
        @JsonProperty("profile_image")
        private String profile_image;
    }
}
