package com.autodrive.backend.dto.sale;

import com.autodrive.backend.entity.sale.SaleContractStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleContractUpdateRequest(
        @PastOrPresent(message = "Contract date cannot be in the future")
        LocalDateTime contractDate,

        @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be greater or equal than zero ")
        BigDecimal salePrice,

        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        String notes,

        SaleContractStatus status) {
}
