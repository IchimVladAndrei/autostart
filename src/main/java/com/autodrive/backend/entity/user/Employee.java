package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="employees")
public class Employee {

    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(name = "employee_role")
    private String employeeRole;

    @NotBlank(message = "CNP is required")
    @Column(unique = true, nullable = false, length = 13)
    private String cnp;

    private String status;

    @NotNull(message = "Base salary is required")
    @PositiveOrZero(message = "Salary must be a positive value or zero")
    @Column(name="base_salary", nullable = false)
    private BigDecimal baseSalary;

    private BigDecimal bonus;

    @NotNull(message = "Hire date is required")
    @Column(name="hire_date", nullable = false)
    private LocalDate hireDate;
}