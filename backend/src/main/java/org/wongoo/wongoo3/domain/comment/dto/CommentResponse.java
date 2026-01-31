package org.wongoo.wongoo3.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.wongoo.wongoo3.domain.comment.Comment;

import java.time.LocalDateTime;

@Schema(description = "댓글 응답 DTO")
public record CommentResponse(
        Long id,
        String content,
        Long authorId,
        String authorNickname,
        Long postId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getAuthor().getNickname(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
