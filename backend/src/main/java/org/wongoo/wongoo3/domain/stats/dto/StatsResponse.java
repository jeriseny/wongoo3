package org.wongoo.wongoo3.domain.stats.dto;

public record StatsResponse(
        long postCount,
        long userCount,
        long commentCount
) {
}
