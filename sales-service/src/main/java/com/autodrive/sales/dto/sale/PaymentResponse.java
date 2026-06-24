package com.autodrive.sales.dto.sale;

import com.autodrive.sales.entity.sale.PaymentStatus;
import com.autodrive.sales.entity.sale.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(BigDecimal amount,
                              PaymentType type,
                              PaymentStatus status,
                              LocalDateTime paymentDate,
                              UUID id,
                              UUID contractId) {
}
