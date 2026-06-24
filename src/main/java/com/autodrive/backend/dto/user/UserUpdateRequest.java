package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Email(message = "Invalid email format")
        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String email,

        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String firstName,

        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String lastName,

        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String phone,

        @Size(min = 6, message = "Password must have at least 6 characters")
        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String password,

        UserRole role
) {
}
