package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.CustomerStatus;

import java.util.UUID;

public record CustomerResponse(String name,
                               String cnp,
                               String phone,
                               CustomerStatus status,
                               UUID userId) {

}
