package org.wongoo.wongoo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.wongoo.wongoo3.domain.post.Post;

import java.time.LocalDateTime;

@Schema(description = "게시글 목록 응답 DTO")
public record PostListResponse(
        Long id,
        String title,
        String authorNickname,
        Long viewCount,
        LocalDateTime createdAt
) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getViewCount(),
                post.getCreatedAt()
        );
    }
}
