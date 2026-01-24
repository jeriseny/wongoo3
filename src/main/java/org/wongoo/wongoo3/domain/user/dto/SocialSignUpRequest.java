package org.wongoo.wongoo3.domain.user.dto;

import org.wongoo.wongoo3.domain.auth.dto.SocialType;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;

import java.util.List;

public record SocialSignUpRequest(
        String email,
        String nickname,
        String phoneNumber,
        String profileImageUrl,
        SocialType socialType,
        String socialId,
        List<TermsRequest> termsAgreeList
) implements SignUpRequest {
}
