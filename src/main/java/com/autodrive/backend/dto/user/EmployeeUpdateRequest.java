package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.EmployeePosition;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        EmployeePosition position,

        @Size(min = 13, max = 13, message = "CNP must be exactly 13 characters")
        String cnp,

        String status,

        @PositiveOrZero(message = "Salary must be a positive value or zero")
        BigDecimal baseSalary,

        @PastOrPresent(message = "Hire date cannot be in the future")
        LocalDate hireDate
) {
}
