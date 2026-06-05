package com.autodrive.backend.dto.auth;

import com.autodrive.backend.entity.user.UserRole;

import java.util.UUID;

public record RegisterResponse(UUID id, String email, String firstName, String lastName, String phone, UserRole userRole) {
}
