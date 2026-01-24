package org.wongoo.wongoo3.domain.user.dto;

/**
 * 사용자 상태를 나타내는 열거형
 * - ACTIVE: 활성 상태
 * - INACTIVE: 비활성 상태
 * - SUSPENDED: 정지 상태
 * - DELETED: 탈퇴 상태
 */
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DELETED
}
