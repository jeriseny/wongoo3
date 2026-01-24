package org.wongoo.wongoo3.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "약관 등록 요청 DTO")
public record AddTermsRequest(

        @Schema(description = "약관 유형", implementation = TermsType.class)
        @NotNull
        TermsType termsType,

        @Schema(description = "약관 내용", example = "이용 약관 내용입니다.")
        @NotBlank
        String content,

        @Schema(description = "필수 여부", example = "true")
        boolean required,
        @Schema(description = "활성화 여부", example = "true")
        boolean isActive
) {
}
