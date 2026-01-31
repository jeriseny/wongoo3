package org.wongoo.wongoo3.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "약관 동의 요청 DTO")
public record TermsRequest(
        @Schema(description = "약관 유형")
        TermsType termsType,
        @Schema(description = "동의 여부", example = "true")
        boolean agreed) {
}
