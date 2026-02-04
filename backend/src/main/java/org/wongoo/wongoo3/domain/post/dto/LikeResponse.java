package org.wongoo.wongoo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 응답 DTO")
public record LikeResponse(
        @Schema(description = "좋아요 수")
        Long likeCount,

        @Schema(description = "현재 사용자의 좋아요 여부")
        Boolean isLiked
) {
}
