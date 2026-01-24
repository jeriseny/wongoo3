package org.wongoo.wongoo3.domain.user.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.dto.LocalSignUpRequest;
import org.wongoo.wongoo3.domain.user.dto.SignUpRequest;
import org.wongoo.wongoo3.domain.user.dto.SocialSignUpRequest;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

@Component
@RequiredArgsConstructor
public class UserSignUpFactory {

    private final PasswordEncoder passwordEncoder;

    public User create(SignUpRequest request) {
        if (request instanceof LocalSignUpRequest local) {
            return User.createLocalUser(
                    local.email(),
                    local.nickname(),
                    local.phoneNumber(),
                    passwordEncoder.encode(local.password())
            );
        }

        if (request instanceof SocialSignUpRequest social) {
            User user = User.createSocialUser(
                    social.email(),
                    social.nickname(),
                    social.phoneNumber(),
                    social.socialType(),
                    social.socialId()
            );
            if (social.profileImageUrl() != null) {
                user.setProfileImageUrl(social.profileImageUrl());
            }
            return user;
        }
        throw new WebErrorException(WebErrorCode.BAD_REQUEST, "알 수 없는 회원가입 요청 타입입니다");
    }
}
