package org.wongoo.wongoo3.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.comment.dto.CommentResponse;
import org.wongoo.wongoo3.domain.comment.dto.CreateCommentRequest;
import org.wongoo.wongoo3.domain.comment.dto.UpdateCommentRequest;
import org.wongoo.wongoo3.domain.comment.service.CommentService;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;
import org.wongoo.wongoo3.global.jwt.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다")
    public ResponseEntity<CommentResponse> createComment(
            @CurrentUser LoginUser loginUser,
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(postId, loginUser.userId(), request));
    }

    @GetMapping("/post/{postId}/comment")
    @Operation(summary = "댓글 목록 조회", description = "게시글의 댓글 목록을 조회합니다")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, pageable));
    }

    @PatchMapping("/comment/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다 (작성자만 가능)")
    public ResponseEntity<CommentResponse> updateComment(
            @CurrentUser LoginUser loginUser,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, loginUser.userId(), request));
    }

    @DeleteMapping("/comment/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다 (작성자만 가능)")
    public ResponseEntity<Void> deleteComment(
            @CurrentUser LoginUser loginUser,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId, loginUser.userId());
        return ResponseEntity.noContent().build();
    }
}
