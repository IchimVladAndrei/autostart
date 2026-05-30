package com.autodrive.backend.entity.sale;

import com.autodrive.backend.entity.user.Customer;
import com.autodrive.backend.entity.car.Vehicle;
import com.autodrive.backend.entity.user.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sales")
public class SaleContract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Contract date is required")
    @Column(name = "contract_date", nullable = false)
    private LocalDateTime contractDate; //automatic or manual?

    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater or equal than zero ")
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private SaleContractStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToOne(optional = false)
    @JoinColumn(name = "vehicle_vin", referencedColumnName = "vin", nullable = false, unique = true)
    private Vehicle vehicle;
}

