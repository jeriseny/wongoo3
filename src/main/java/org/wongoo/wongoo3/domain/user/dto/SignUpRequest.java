package org.wongoo.wongoo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;

import java.util.List;

@Schema(description = "회원가입 요청 (일반/소셜 공통)")
public interface SignUpRequest {

    String email();
    String nickname();
    String phoneNumber();
    List<TermsRequest> termsAgreeList();
}
