package com.autodrive.authuser.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
@RefreshScope
@Service
public class InternalTokenService {

    private final String internalToken;

    public InternalTokenService(@Value("${app.internal.token}") String internalToken) {
        this.internalToken = internalToken;
    }

    public void validate(String token) {
        if (token == null || !token.equals(internalToken)) {
            throw new AccessDeniedException("Invalid internal service token");
        }
    }
}
