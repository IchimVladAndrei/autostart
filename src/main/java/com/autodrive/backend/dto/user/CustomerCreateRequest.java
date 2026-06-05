package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CNP is required")
        @Size(min = 13, max = 13, message = "CNP must be exactly 13 characters")
        String cnp,

        @NotBlank(message = "Phone is required")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        @Pattern(regexp = "^[0-9+()\\s-]+$", message = "Phone number format invalid")
        String phone,

        @NotNull(message = "Status is required")
        CustomerStatus status
) {
}