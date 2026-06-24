package com.autodrive.sales.entity.sale;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@Table(name = "sales", schema = "sales")
public class SaleContract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Contract date is required")
    @PastOrPresent(message = "Contract date cannot be in the future")
    @Column(name = "contract_date", nullable = false)
    private LocalDateTime contractDate; //automatic or manual?

    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater or equal than zero ")
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private SaleContractStatus status;

    @NotNull(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @NotNull(message = "Employee ID is required")
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @NotNull(message = "Vehicle VIN is required")
    @Column(name = "vehicle_vin", nullable = false, unique = true, length = 17)
    private String vehicleVin;
}
