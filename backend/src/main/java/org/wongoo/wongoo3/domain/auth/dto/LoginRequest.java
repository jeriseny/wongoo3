package org.wongoo.wongoo3.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        @Schema(description = "이메일", example = "aeternus0519@naver.com")
        String email,
        @NotBlank
        @Schema(description = "비밀번호", example = "15730254791!a")
        String password,
        boolean rememberMe
) {
}
