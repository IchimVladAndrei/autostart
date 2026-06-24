package com.autodrive.sales.dto.sale;

import com.autodrive.sales.entity.sale.SaleContractStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SaleContractCreateRequest(@NotNull(message = "Contract date is required")
                                        @PastOrPresent(message = "Contract date cannot be in the future")
                                        LocalDateTime contractDate,

                                        @NotNull(message = "Sale price is required")
                                        @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater or equal than zero ")
                                        BigDecimal salePrice,

                                        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
                                        String notes,

                                        @NotNull(message = "Status is required")
                                        SaleContractStatus status,
                                        @NotBlank(message = "Vehicle VIN is required")
                                        String vehicleVin,

                                        @NotNull(message = "Customer ID is required")
                                        UUID customerId,

                                        @NotNull(message = "Employee ID is required")
                                        UUID employeeId) {
}
