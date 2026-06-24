package com.autodrive.backend.dto.sale;

import com.autodrive.backend.entity.sale.PaymentStatus;
import com.autodrive.backend.entity.sale.PaymentType;

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
