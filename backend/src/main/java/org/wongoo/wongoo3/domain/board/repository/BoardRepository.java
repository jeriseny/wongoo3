package org.wongoo.wongoo3.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findBySlug(String slug);
    List<Board> findAllByIsActiveTrueOrderByDisplayOrderAsc();
    boolean existsBySlug(String slug);
}
