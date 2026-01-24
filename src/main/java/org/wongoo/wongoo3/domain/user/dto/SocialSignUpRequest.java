package org.wongoo.wongoo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.terms.dto.TermsRequest;

import java.util.List;

@Schema(description = "소셜 회원 가입 요청 DTO")
public record SocialSignUpRequest(
        @Email
        @Schema(description = "이메일", example = "aeternus0520@naver.com")
        String email,
        @NotBlank
        @Schema(description = "닉네임", example = "코틀린의 제왕")
        String nickname,
        @NotBlank
        @Schema(description = "전화번호", example = "010-3082-1298")
        String phoneNumber,

        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl,

        @NotNull
        @Schema(description = "소셜 제공자 타입", example = "NAVER")
        ProviderType providerType,

        @NotBlank
        @Schema(description = "소셜 제공자 ID", example = "xx-xx-xxxx-xxx")
        String providerId,
        @ArraySchema(
                schema = @Schema(implementation = TermsRequest.class),
                arraySchema = @Schema(description = "약관 동의 목록")
        )
        List<TermsRequest> termsAgreeList
) implements SignUpRequest {
}
