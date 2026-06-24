package com.autodrive.backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, long expirationMs, long refreshExpirationMs, long rememberMeRefreshExpirationMs) {
    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret must be configured");
        }
        if (expirationMs <= 0) {
            throw new IllegalArgumentException("JWT expiration must be positive");
        }
        if (refreshExpirationMs <= 0) {
            throw new IllegalArgumentException("JWT refresh expiration must be positive");
        }
        if (rememberMeRefreshExpirationMs <= refreshExpirationMs) {
            throw new IllegalArgumentException("Remember-me refresh expiration must be greater than the default refresh expiration");
        }
    }
}
