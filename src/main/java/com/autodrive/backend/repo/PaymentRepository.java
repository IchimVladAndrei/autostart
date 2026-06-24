package com.autodrive.backend.repo;

import com.autodrive.backend.entity.sale.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Override
    @EntityGraph(attributePaths = "contract")
    Page<Payment> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "contract")
    Optional<Payment> findById(UUID id);

    boolean existsByContractId(UUID contractId);
}
