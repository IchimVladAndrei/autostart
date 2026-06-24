package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "employees")
public class Employee {

    @Id
    private UUID userId;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Position is required")
    @Column(name = "position")
    private EmployeePosition position;

    @NotBlank(message = "CNP is required")
    @Pattern(regexp = "^\\d{13}$", message = "CNP must contain exactly 13 digits")
    @Column(unique = true, nullable = false, length = 13)
    private String cnp;

    private String status;

    @NotNull(message = "Base salary is required")
    @PositiveOrZero(message = "Salary must be a positive value or zero")
    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;

    @PositiveOrZero(message = "Bonus must be a positive value or zero")
    private BigDecimal bonus;

    @NotNull(message = "Hire date is required")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
}
