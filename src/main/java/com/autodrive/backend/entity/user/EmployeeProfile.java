package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name="employee_profiles")
public class EmployeeProfile {
    @Id
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
    @NotBlank(message = "temp")
    @Size(min=13,max=13,message = "temp")
    @Column(unique = true,nullable = false,length = 13)
    private String cnp;
    @NotBlank(message = "temp")
    @Column(name="hire_date",nullable = false)
    private LocalDate hireDate;
    @NotNull(message = "temp")
    @Positive(message = "temp")
    @Column(name="base_salary",nullable = false)
    private BigDecimal baseSalary;
}
