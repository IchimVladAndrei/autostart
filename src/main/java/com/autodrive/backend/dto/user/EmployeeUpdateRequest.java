package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.EmployeePosition;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        EmployeePosition position,

        @Pattern(regexp = "^\\d{13}$", message = "CNP must contain exactly 13 digits")
        String cnp,

        String status,

        @PositiveOrZero(message = "Salary must be a positive value or zero")
        BigDecimal baseSalary,

        @PositiveOrZero(message = "Bonus must be a positive value or zero")
        BigDecimal bonus,

        @PastOrPresent(message = "Hire date cannot be in the future")
        LocalDate hireDate
) {
}
