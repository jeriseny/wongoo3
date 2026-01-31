package org.wongoo.wongoo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 수정 요청 DTO")
public record UpdatePostRequest(
        @NotBlank
        @Size(max = 200)
        @Schema(description = "게시글 제목")
        String title,

        @NotBlank
        @Schema(description = "게시글 내용")
        String content
) {}
