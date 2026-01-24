package org.wongoo.wongoo3.domain.user.dto;

import org.wongoo.wongoo3.domain.auth.dto.UserRole;

public record LoginUser(
        Long userId,
        String email,
        UserRole role
) {
}
