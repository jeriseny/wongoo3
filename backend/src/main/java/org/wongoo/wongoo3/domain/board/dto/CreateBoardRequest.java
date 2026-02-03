package org.wongoo.wongoo3.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "게시판 생성 요청 DTO")
public record CreateBoardRequest(
    @NotBlank
    @Size(max = 100)
    @Schema(description = "게시판 이름", example = "자유게시판")
    String name,

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z0-9-]+$", message = "slug는 영문 소문자, 숫자, 하이픈만 허용됩니다")
    @Schema(description = "URL용 슬러그", example = "free")
    String slug,

    @Size(max = 500)
    @Schema(description = "게시판 설명", example = "자유롭게 글을 작성하세요")
    String description
) {}
