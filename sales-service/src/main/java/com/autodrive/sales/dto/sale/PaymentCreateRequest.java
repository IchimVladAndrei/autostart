package com.autodrive.sales.dto.sale;

import com.autodrive.sales.entity.sale.PaymentStatus;
import com.autodrive.sales.entity.sale.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentCreateRequest(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Amount must have up to 2 fractional digits")
        BigDecimal amount,

        @NotNull(message = "Payment type is required")
        PaymentType type,

        @NotNull(message = "Payment status is required")
        PaymentStatus status,

        @NotNull(message = "Payment date is required")
        @PastOrPresent(message = "Payment date cannot be in the future")
        LocalDateTime paymentDate,
        @NotNull(message = "Contract ID is required")
        UUID contractId
) {
}
