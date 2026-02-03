package org.wongoo.wongoo3.domain.post.dto;

public record PostSearchRequest(
        String boardSlug,
        SearchType searchType,
        String keyword,
        SortType sortBy
) {
    public PostSearchRequest {
        if (sortBy == null) {
            sortBy = SortType.LATEST;
        }
    }
}
