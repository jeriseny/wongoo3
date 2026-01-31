package org.wongoo.wongoo3.domain.user.dto;

import org.wongoo.wongoo3.domain.auth.dto.UserRole;
import org.wongoo.wongoo3.domain.auth.oauth2.dto.ProviderType;
import org.wongoo.wongoo3.domain.user.User;

import java.time.LocalDateTime;

public record UserInfoResponse(
        String email,
        String nickName,
        String phoneNumber,
        String profileImageUrl,
        UserRole role,
        boolean isSocial,
        ProviderType providerType,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                user.getRole(),
                user.getProviderType() != null,
                user.getProviderType(),
                user.getLastLoginAt(),
                user.getCreatedAt()
        );
    }
}
