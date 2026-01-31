package org.wongoo.wongoo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;

import java.util.List;

@Schema(description = "로컬 회원 가입 요청 DTO")
public record LocalSignUpRequest(
        @Email
        @Schema(description = "이메일", example = "aeternus0519@naver.com")
        String email,
        @NotBlank
        @Schema(description = "닉네임", example = "자바의제왕")
        String nickname,
        @NotBlank
        @Schema(description = "전화번호", example = "010-2446-9149")
        String phoneNumber,
        @NotBlank
        @Schema(description = "비밀번호", example = "15730254791!a")
        String password,
        @ArraySchema(
                schema = @Schema(implementation = TermsRequest.class),
                arraySchema = @Schema(description = "약관 동의 목록")
        )
        List<TermsRequest> termsAgreeList
) implements SignUpRequest {
}
