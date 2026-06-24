package com.autodrive.authuser.dto.user;

import com.autodrive.authuser.entity.user.CustomerStatus;

import java.util.UUID;

public record CustomerResponse(String name,
                               String cnp,
                               String phone,
                               CustomerStatus status,
                               UUID userId) {

}
