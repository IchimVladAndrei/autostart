package com.autodrive.backend.controller;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.sale.PaymentCreateRequest;
import com.autodrive.backend.dto.sale.PaymentResponse;
import com.autodrive.backend.dto.sale.PaymentUpdateRequest;
import com.autodrive.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_FINANCE_SPECIALIST')")
    public PageResponse<PaymentResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "paymentDate") String sortBy,
                                                @RequestParam(defaultValue = "desc") String direction) {
        return paymentService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_FINANCE_SPECIALIST')")
    public PaymentResponse getById(@PathVariable UUID id) {
        return paymentService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_FINANCE_SPECIALIST')")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_FINANCE_SPECIALIST')")
    public PaymentResponse update(@PathVariable UUID id, @Valid @RequestBody PaymentUpdateRequest request) {
        return paymentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_FINANCE_SPECIALIST')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
