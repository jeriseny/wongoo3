package org.wongoo.wongoo3.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wongoo.wongoo3.domain.board.Board;
import org.wongoo.wongoo3.domain.board.dto.BoardResponse;
import org.wongoo.wongoo3.domain.board.dto.CreateBoardRequest;
import org.wongoo.wongoo3.domain.board.repository.BoardRepository;
import org.wongoo.wongoo3.global.exception.WebErrorCode;
import org.wongoo.wongoo3.global.exception.WebErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Cacheable(value = "boards", key = "'active'")
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllActiveBoards() {
        return boardRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc()
            .stream()
            .map(BoardResponse::from)
            .toList();
    }

    @Cacheable(value = "boards", key = "#slug")
    @Transactional(readOnly = true)
    public BoardResponse getBoardBySlug(String slug) {
        Board board = findBySlug(slug);
        return BoardResponse.from(board);
    }

    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request) {
        if (boardRepository.existsBySlug(request.slug())) {
            throw new WebErrorException(WebErrorCode.BAD_REQUEST, "이미 존재하는 슬러그입니다: " + request.slug());
        }
        Board board = Board.create(request.name(), request.slug(), request.description());
        boardRepository.save(board);
        return BoardResponse.from(board);
    }

    public Board findBySlug(String slug) {
        return boardRepository.findBySlug(slug)
            .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "게시판을 찾을 수 없습니다: " + slug));
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new WebErrorException(WebErrorCode.NOT_FOUND, "게시판을 찾을 수 없습니다: " + id));
    }
}
