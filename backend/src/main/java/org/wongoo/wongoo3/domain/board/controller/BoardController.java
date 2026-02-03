package org.wongoo.wongoo3.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wongoo.wongoo3.domain.board.dto.BoardResponse;
import org.wongoo.wongoo3.domain.board.dto.CreateBoardRequest;
import org.wongoo.wongoo3.domain.board.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시판", description = "게시판 관련 API")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    @Operation(summary = "게시판 목록 조회", description = "활성화된 모든 게시판을 조회합니다")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllActiveBoards());
    }

    @GetMapping("/{slug}")
    @Operation(summary = "게시판 조회", description = "슬러그로 게시판을 조회합니다")
    public ResponseEntity<BoardResponse> getBoardBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(boardService.getBoardBySlug(slug));
    }

    @PostMapping
    @Operation(summary = "게시판 생성", description = "새 게시판을 생성합니다 (관리자 전용)")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.ok(boardService.createBoard(request));
    }
}
