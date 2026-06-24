package com.autodrive.authuser.dto.user;

import com.autodrive.authuser.entity.user.EmployeePosition;

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
