package com.autodrive.authuser.dto.auth;

import com.autodrive.authuser.entity.user.UserRole;

import java.util.UUID;

public record RegisterResponse(UUID id, String email, String firstName, String lastName, String phone, UserRole role) {
}
