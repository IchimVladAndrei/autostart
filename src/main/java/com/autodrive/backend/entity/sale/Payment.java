package com.autodrive.backend.entity.sale;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have up to 2 fractional digits")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment type is required")
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be in the future")
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate; // automatic or manual insertion?

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    @NotNull(message = "Contract is required")
    private SaleContract contract;
}