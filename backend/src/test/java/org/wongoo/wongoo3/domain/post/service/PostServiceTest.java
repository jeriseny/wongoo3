package org.wongoo.wongoo3.domain.post.service;

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
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.dto.CreatePostRequest;
import org.wongoo.wongoo3.domain.post.dto.PostListResponse;
import org.wongoo.wongoo3.domain.post.dto.PostResponse;
import org.wongoo.wongoo3.domain.post.dto.UpdatePostRequest;
import org.wongoo.wongoo3.domain.post.repository.PostRepository;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;
import org.wongoo.wongoo3.global.exception.WebErrorException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;
    User anotherUser;

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
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void createPost() {
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용입니다.");

        PostResponse response = postService.createPost(testUser.getId(), request);

        assertNotNull(response.id());
        assertEquals("테스트 제목", response.title());
        assertEquals("테스트 내용입니다.", response.content());
        assertEquals(testUser.getId(), response.authorId());
        assertEquals(testUser.getNickname(), response.authorNickname());
        assertEquals(0L, response.viewCount());
    }

    @Test
    @DisplayName("게시글 조회 및 조회수 증가 테스트")
    void getPostWithViewCount() {
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용입니다.");
        PostResponse created = postService.createPost(testUser.getId(), request);

        PostResponse response1 = postService.getPostWithViewCount(created.id());
        assertEquals(1L, response1.viewCount());

        PostResponse response2 = postService.getPostWithViewCount(created.id());
        assertEquals(2L, response2.viewCount());
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트")
    void getPostList() {
        for (int i = 1; i <= 15; i++) {
            CreatePostRequest request = new CreatePostRequest("제목 " + i, "내용 " + i);
            postService.createPost(testUser.getId(), request);
        }

        Page<PostListResponse> page1 = postService.getPostList(PageRequest.of(0, 10));
        assertEquals(10, page1.getContent().size());
        assertEquals(15, page1.getTotalElements());
        assertEquals(2, page1.getTotalPages());

        Page<PostListResponse> page2 = postService.getPostList(PageRequest.of(1, 10));
        assertEquals(5, page2.getContent().size());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost() {
        CreatePostRequest createRequest = new CreatePostRequest("원래 제목", "원래 내용");
        PostResponse created = postService.createPost(testUser.getId(), createRequest);

        UpdatePostRequest updateRequest = new UpdatePostRequest("수정된 제목", "수정된 내용");
        PostResponse updated = postService.updatePost(created.id(), testUser.getId(), updateRequest);

        assertEquals("수정된 제목", updated.title());
        assertEquals("수정된 내용", updated.content());
    }

    @Test
    @DisplayName("다른 사용자의 게시글 수정 시도 시 예외 발생 테스트")
    void updatePostByAnotherUser() {
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용");
        PostResponse created = postService.createPost(testUser.getId(), request);

        UpdatePostRequest updateRequest = new UpdatePostRequest("수정 시도", "수정 시도");

        assertThrows(WebErrorException.class, () ->
                postService.updatePost(created.id(), anotherUser.getId(), updateRequest)
        );
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost() {
        CreatePostRequest request = new CreatePostRequest("삭제할 게시글", "삭제될 내용");
        PostResponse created = postService.createPost(testUser.getId(), request);

        postService.deletePost(created.id(), testUser.getId());

        assertFalse(postRepository.existsById(created.id()));
    }

    @Test
    @DisplayName("다른 사용자의 게시글 삭제 시도 시 예외 발생 테스트")
    void deletePostByAnotherUser() {
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용");
        PostResponse created = postService.createPost(testUser.getId(), request);

        assertThrows(WebErrorException.class, () ->
                postService.deletePost(created.id(), anotherUser.getId())
        );
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외 발생 테스트")
    void getNotExistPost() {
        assertThrows(WebErrorException.class, () ->
                postService.getPost(999999L)
        );
    }
}
