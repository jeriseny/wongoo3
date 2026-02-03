package org.wongoo.wongoo3.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.dto.PostSearchRequest;

public interface PostRepositoryCustom {
    Page<Post> search(PostSearchRequest request, Pageable pageable);
}
