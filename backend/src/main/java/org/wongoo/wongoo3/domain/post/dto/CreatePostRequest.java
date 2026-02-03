package org.wongoo.wongoo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 생성 요청 DTO")
public record CreatePostRequest(
        @NotBlank
        @Size(max = 200)
        @Schema(description = "게시글 제목", example = "첫 번째 게시글입니다")
        String title,

        @NotBlank
        @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
        String content,

        @NotBlank
        @Schema(description = "게시판 슬러그", example = "free")
        String boardSlug
) {}
