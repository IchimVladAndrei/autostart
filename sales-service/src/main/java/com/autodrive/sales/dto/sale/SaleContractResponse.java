package com.autodrive.sales.dto.sale;

import com.autodrive.sales.entity.sale.SaleContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SaleContractResponse(
        LocalDateTime contractDate,
        BigDecimal salePrice,
        String notes,
        SaleContractStatus status,
        UUID id,
        String vehicleVin,
        UUID customerId,
        UUID employeeId) {
}
