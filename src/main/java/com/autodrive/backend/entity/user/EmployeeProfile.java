package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
@Table(name = "employee_profiles")
public class EmployeeProfile {
    @Id
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
    @NotBlank(message = "CNP is required")
    @Size(min = 13, max = 13, message = "CNP must be exactly 13 characters long")
    @Column(unique = true, nullable = false, length = 13)
    private String cnp;
    @NotBlank(message = "Hire date is required")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    @NotNull(message = "Base salary is required")
    @PositiveOrZero(message = "Base salary must be a positive number")
    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;
}
