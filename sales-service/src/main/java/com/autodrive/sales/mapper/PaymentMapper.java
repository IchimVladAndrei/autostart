package com.autodrive.sales.mapper;

import com.autodrive.sales.dto.sale.PaymentCreateRequest;
import com.autodrive.sales.dto.sale.PaymentResponse;
import com.autodrive.sales.dto.sale.PaymentUpdateRequest;
import com.autodrive.sales.entity.sale.Payment;

public final class PaymentMapper {

    private PaymentMapper() {

    }

    public static Payment toEntity(PaymentCreateRequest req) {
        return Payment
                .builder()
                .amount(req.amount())
                .type(req.type())
                .status(req.status())
                .paymentDate(req.paymentDate())
                .build();
    }

    public static PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getAmount(),
                p.getType(),
                p.getStatus(),
                p.getPaymentDate(),
                p.getId(),
                p.getContract() != null ? p.getContract().getId() : null
        );
    }


    public static void applyUpdates(Payment p, PaymentUpdateRequest req) {
        if (req.amount() != null) p.setAmount(req.amount());
        if (req.type() != null) p.setType(req.type());
        if (req.status() != null) p.setStatus(req.status());
        if (req.paymentDate() != null) p.setPaymentDate(req.paymentDate());
    }
}
