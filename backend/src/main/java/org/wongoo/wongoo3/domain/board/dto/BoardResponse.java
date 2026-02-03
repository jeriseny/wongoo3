package org.wongoo.wongoo3.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.wongoo.wongoo3.domain.board.Board;

@Schema(description = "게시판 응답 DTO")
public record BoardResponse(
    @Schema(description = "게시판 ID")
    Long id,

    @Schema(description = "게시판 이름")
    String name,

    @Schema(description = "게시판 슬러그")
    String slug,

    @Schema(description = "게시판 설명")
    String description
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
            board.getId(),
            board.getName(),
            board.getSlug(),
            board.getDescription()
        );
    }
}
