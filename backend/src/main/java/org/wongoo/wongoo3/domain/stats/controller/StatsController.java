package org.wongoo.wongoo3.domain.stats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongoo.wongoo3.domain.comment.repository.CommentRepository;
import org.wongoo.wongoo3.domain.post.repository.PostRepository;
import org.wongoo.wongoo3.domain.stats.dto.StatsResponse;
import org.wongoo.wongoo3.domain.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
@Tag(name = "통계", description = "커뮤니티 통계 API")
public class StatsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @GetMapping
    @Operation(summary = "커뮤니티 통계 조회", description = "게시글, 회원, 댓글 수를 조회합니다")
    public ResponseEntity<StatsResponse> getStats() {
        long postCount = postRepository.count();
        long userCount = userRepository.count();
        long commentCount = commentRepository.count();
        return ResponseEntity.ok(new StatsResponse(postCount, userCount, commentCount));
    }
}
