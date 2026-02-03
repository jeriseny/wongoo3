package org.wongoo.wongoo3.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.post.dto.CreatePostRequest;
import org.wongoo.wongoo3.domain.post.dto.PostListResponse;
import org.wongoo.wongoo3.domain.post.dto.PostResponse;
import org.wongoo.wongoo3.domain.post.dto.PostSearchRequest;
import org.wongoo.wongoo3.domain.post.dto.SearchType;
import org.wongoo.wongoo3.domain.post.dto.SortType;
import org.wongoo.wongoo3.domain.post.dto.UpdatePostRequest;
import org.wongoo.wongoo3.domain.post.service.PostService;
import org.wongoo.wongoo3.domain.user.dto.LoginUser;
import org.wongoo.wongoo3.global.jwt.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "게시글", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다")
    public ResponseEntity<PostResponse> createPost(
            @CurrentUser LoginUser loginUser,
            @Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.createPost(loginUser.userId(), request));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회", description = "게시글 상세 정보를 조회합니다")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostWithViewCount(postId));
    }

    @GetMapping
    @Operation(summary = "게시글 목록/검색", description = "게시글 목록을 검색/필터링하여 조회합니다")
    public ResponseEntity<Page<PostListResponse>> getPostList(
            @RequestParam(required = false) String boardSlug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "LATEST") SortType sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        PostSearchRequest request = new PostSearchRequest(boardSlug, searchType, keyword, sortBy);
        return ResponseEntity.ok(postService.searchPosts(request, pageable));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다 (작성자만 가능)")
    public ResponseEntity<PostResponse> updatePost(
            @CurrentUser LoginUser loginUser,
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, loginUser.userId(), request));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다 (작성자만 가능)")
    public ResponseEntity<Void> deletePost(
            @CurrentUser LoginUser loginUser,
            @PathVariable Long postId) {
        postService.deletePost(postId, loginUser.userId());
        return ResponseEntity.noContent().build();
    }
}
