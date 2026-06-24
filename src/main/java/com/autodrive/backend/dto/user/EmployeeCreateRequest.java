package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.EmployeePosition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeCreateRequest(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Position is required")
        EmployeePosition position,

        @NotBlank(message = "CNP is required")
        @Pattern(regexp = "^\\d{13}$", message = "CNP must contain exactly 13 digits")
        String cnp,

        String status,

        @NotNull(message = "Base salary is required")
        @PositiveOrZero(message = "Salary must be a positive value or zero")
        BigDecimal baseSalary,

        @PositiveOrZero(message = "Bonus must be a positive value or zero")
        BigDecimal bonus,

        @NotNull(message = "Hire date is required")
        @PastOrPresent(message = "Hire date cannot be in the future")
        LocalDate hireDate
) {
}
