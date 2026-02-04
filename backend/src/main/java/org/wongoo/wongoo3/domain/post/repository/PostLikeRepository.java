package org.wongoo.wongoo3.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.post.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);

    void deleteByPostIdAndUserId(Long postId, Long userId);

    void deleteAllByPostId(Long postId);
}
