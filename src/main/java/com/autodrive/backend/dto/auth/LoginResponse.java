package com.autodrive.backend.dto.auth;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
