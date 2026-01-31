package org.wongoo.wongoo3.domain.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.comment.dto.CommentResponse;
import org.wongoo.wongoo3.domain.comment.dto.CreateCommentRequest;
import org.wongoo.wongoo3.domain.comment.dto.UpdateCommentRequest;
import org.wongoo.wongoo3.domain.comment.repository.CommentRepository;
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.repository.PostRepository;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.global.exception.WebErrorException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;
    User anotherUser;
    Post testPost;

    @BeforeEach
    void setUp() {
        testUser = User.createLocalUser(
                "test@example.com",
                "테스트유저",
                "010-1234-5678",
                "password123!"
        );
        userRepository.save(testUser);

        anotherUser = User.createLocalUser(
                "another@example.com",
                "다른유저",
                "010-9876-5432",
                "password456!"
        );
        userRepository.save(anotherUser);

        testPost = Post.create("테스트 게시글", "테스트 게시글 내용입니다.", testUser);
        postRepository.save(testPost);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void createComment() {
        CreateCommentRequest request = new CreateCommentRequest("테스트 댓글입니다.");

        CommentResponse response = commentService.createComment(testPost.getId(), testUser.getId(), request);

        assertNotNull(response.id());
        assertEquals("테스트 댓글입니다.", response.content());
        assertEquals(testUser.getId(), response.authorId());
        assertEquals(testUser.getNickname(), response.authorNickname());
        assertEquals(testPost.getId(), response.postId());
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    void getCommentsByPost() {
        for (int i = 1; i <= 25; i++) {
            CreateCommentRequest request = new CreateCommentRequest("댓글 " + i);
            commentService.createComment(testPost.getId(), testUser.getId(), request);
        }

        Page<CommentResponse> page1 = commentService.getCommentsByPost(testPost.getId(), PageRequest.of(0, 20));
        assertEquals(20, page1.getContent().size());
        assertEquals(25, page1.getTotalElements());
        assertEquals(2, page1.getTotalPages());

        Page<CommentResponse> page2 = commentService.getCommentsByPost(testPost.getId(), PageRequest.of(1, 20));
        assertEquals(5, page2.getContent().size());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateComment() {
        CreateCommentRequest createRequest = new CreateCommentRequest("원래 댓글");
        CommentResponse created = commentService.createComment(testPost.getId(), testUser.getId(), createRequest);

        UpdateCommentRequest updateRequest = new UpdateCommentRequest("수정된 댓글");
        CommentResponse updated = commentService.updateComment(created.id(), testUser.getId(), updateRequest);

        assertEquals("수정된 댓글", updated.content());
    }

    @Test
    @DisplayName("다른 사용자의 댓글 수정 시도 시 예외 발생 테스트")
    void updateCommentByAnotherUser() {
        CreateCommentRequest request = new CreateCommentRequest("테스트 댓글");
        CommentResponse created = commentService.createComment(testPost.getId(), testUser.getId(), request);

        UpdateCommentRequest updateRequest = new UpdateCommentRequest("수정 시도");

        assertThrows(WebErrorException.class, () ->
                commentService.updateComment(created.id(), anotherUser.getId(), updateRequest)
        );
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComment() {
        CreateCommentRequest request = new CreateCommentRequest("삭제할 댓글");
        CommentResponse created = commentService.createComment(testPost.getId(), testUser.getId(), request);

        commentService.deleteComment(created.id(), testUser.getId());

        assertFalse(commentRepository.existsById(created.id()));
    }

    @Test
    @DisplayName("다른 사용자의 댓글 삭제 시도 시 예외 발생 테스트")
    void deleteCommentByAnotherUser() {
        CreateCommentRequest request = new CreateCommentRequest("테스트 댓글");
        CommentResponse created = commentService.createComment(testPost.getId(), testUser.getId(), request);

        assertThrows(WebErrorException.class, () ->
                commentService.deleteComment(created.id(), anotherUser.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 작성 시 예외 발생 테스트")
    void createCommentOnNotExistPost() {
        CreateCommentRequest request = new CreateCommentRequest("테스트 댓글");

        assertThrows(WebErrorException.class, () ->
                commentService.createComment(999999L, testUser.getId(), request)
        );
    }
}
