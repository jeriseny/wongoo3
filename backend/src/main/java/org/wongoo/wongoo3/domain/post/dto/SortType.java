package org.wongoo.wongoo3.domain.post.dto;

public enum SortType {
    LATEST,     // 최신순 (createdAt DESC)
    VIEW_COUNT  // 조회수순 (viewCount DESC)
}
