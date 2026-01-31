package org.wongoo.wongoo3.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "댓글 수정 요청 DTO")
public record UpdateCommentRequest(
        @NotBlank
        @Schema(description = "댓글 내용")
        String content
) {}
