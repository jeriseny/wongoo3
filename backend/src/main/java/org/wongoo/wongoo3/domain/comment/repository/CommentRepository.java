package org.wongoo.wongoo3.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<Comment> findByPostIdOrderByCreatedAtAsc(Long postId, Pageable pageable);

    Long countByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
