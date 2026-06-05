package com.autodrive.backend.dto.sale;

import com.autodrive.backend.entity.sale.PaymentStatus;
import com.autodrive.backend.entity.sale.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentUpdateRequest(
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Amount must have up to 2 fractional digits")
        BigDecimal amount,

        PaymentType type,

        PaymentStatus status,

        @PastOrPresent(message = "Payment date cannot be in the future")
        LocalDateTime paymentDate
) {
}
