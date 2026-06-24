package com.autodrive.sales.service;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.sales.dto.sale.PaymentCreateRequest;
import com.autodrive.sales.dto.sale.PaymentResponse;
import com.autodrive.sales.dto.sale.PaymentUpdateRequest;
import com.autodrive.sales.entity.sale.Payment;
import com.autodrive.sales.entity.sale.SaleContract;
import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.sales.mapper.PaymentMapper;
import com.autodrive.sales.repo.PaymentRepository;
import com.autodrive.sales.repo.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private static final Set<String> SORTS = Set.of("id", "amount", "type", "status", "paymentDate");

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(paymentRepository.findAll(pageable).map(PaymentMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PaymentResponse findById(UUID id) {
        return PaymentMapper.toResponse(getPayment(id));
    }

    public PaymentResponse create(PaymentCreateRequest request) {
        SaleContract contract = saleRepository.findById(request.contractId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale contract not found with id: " + request.contractId()));
        Payment payment = PaymentMapper.toEntity(request);
        payment.setContract(contract);
        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse update(UUID id, PaymentUpdateRequest request) {
        Payment payment = getPayment(id);
        PaymentMapper.applyUpdates(payment, request);
        return PaymentMapper.toResponse(paymentRepository.save(payment));
    }

    public void delete(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private Payment getPayment(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }
}
