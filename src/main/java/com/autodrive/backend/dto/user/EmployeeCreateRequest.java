package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.EmployeePosition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Position is required")
        EmployeePosition position,

        @NotBlank(message = "CNP is required")
        @Size(min = 13, max = 13, message = "CNP must be exactly 13 characters")
        String cnp,

        String status,

        @NotNull(message = "Base salary is required")
        @PositiveOrZero(message = "Salary must be a positive value or zero")
        BigDecimal baseSalary,

        @NotNull(message = "Hire date is required")
        @PastOrPresent(message = "Hire date cannot be in the future")
        LocalDate hireDate
) {
}
