package com.autodrive.authuser.dto.user;

import com.autodrive.authuser.entity.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole role,
        LocalDateTime createdAt
) {
}
