package org.wongoo.wongoo3.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.comment.Comment;
import org.wongoo.wongoo3.domain.comment.dto.CommentResponse;
import org.wongoo.wongoo3.domain.comment.dto.CreateCommentRequest;
import org.wongoo.wongoo3.domain.comment.dto.UpdateCommentRequest;
import org.wongoo.wongoo3.domain.comment.repository.CommentRepository;
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.service.PostService;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CreateCommentRequest request) {
        Post post = postService.getPostById(postId);
        User author = userService.getUserById(userId);
        Comment comment = Comment.create(request.content(), author, post);
        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        postService.getPostById(postId);
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId, pageable)
                .map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, UpdateCommentRequest request) {
        Comment comment = getCommentById(commentId);
        validateAuthor(comment, userId);
        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = getCommentById(commentId);
        validateAuthor(comment, userId);
        commentRepository.delete(comment);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND,
                        "해당 ID의 댓글을 찾을 수 없습니다: " + commentId));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.isAuthor(userId)) {
            throw new WebErrorException(WebErrorCode.FORBIDDEN,
                    "댓글 작성자만 수정/삭제할 수 있습니다");
        }
    }
}
