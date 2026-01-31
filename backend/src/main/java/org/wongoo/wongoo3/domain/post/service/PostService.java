package org.wongoo.wongoo3.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.comment.repository.CommentRepository;
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.dto.CreatePostRequest;
import org.wongoo.wongoo3.domain.post.dto.PostListResponse;
import org.wongoo.wongoo3.domain.post.dto.PostResponse;
import org.wongoo.wongoo3.domain.post.dto.UpdatePostRequest;
import org.wongoo.wongoo3.domain.post.repository.PostRepository;
import org.wongoo.wongoo3.domain.user.User;
import org.wongoo.wongoo3.domain.user.service.UserService;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse createPost(Long userId, CreatePostRequest request) {
        User author = userService.getUserById(userId);
        Post post = Post.create(request.title(), request.content(), author);
        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = getPostById(postId);
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse getPostWithViewCount(Long postId) {
        Post post = getPostById(postId);
        post.incrementViewCount();
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(PostListResponse::from);
    }

    @Transactional
    public PostResponse updatePost(Long postId, Long userId, UpdatePostRequest request) {
        Post post = getPostById(postId);
        validateAuthor(post, userId);
        post.update(request.title(), request.content());
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        validateAuthor(post, userId);
        commentRepository.deleteAllByPostId(postId);
        postRepository.delete(post);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND,
                        "해당 ID의 게시글을 찾을 수 없습니다: " + postId));
    }

    private void validateAuthor(Post post, Long userId) {
        if (!post.isAuthor(userId)) {
            throw new WebErrorException(WebErrorCode.FORBIDDEN,
                    "게시글 작성자만 수정/삭제할 수 있습니다");
        }
    }
}
