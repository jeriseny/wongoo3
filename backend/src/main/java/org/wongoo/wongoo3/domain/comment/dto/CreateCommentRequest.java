package org.wongoo.wongoo3.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "댓글 생성 요청 DTO")
public record CreateCommentRequest(
        @NotBlank
        @Schema(description = "댓글 내용", example = "좋은 글이네요!")
        String content
) {}
