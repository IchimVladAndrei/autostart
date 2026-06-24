package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.EmployeePosition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(String name,
                               String cnp,
                               EmployeePosition position,

                               String status,
                               BigDecimal baseSalary,
                               BigDecimal bonus,
                               LocalDate hireDate,
                               UUID userId) {
}
