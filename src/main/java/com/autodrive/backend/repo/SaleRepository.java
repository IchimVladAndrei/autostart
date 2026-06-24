package com.autodrive.backend.repo;

import com.autodrive.backend.entity.sale.SaleContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleContract, UUID> {
    @Override
    @EntityGraph(attributePaths = {"customer", "employee", "vehicle"})
    Page<SaleContract> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"customer", "employee", "vehicle"})
    Optional<SaleContract> findById(UUID id);

    boolean existsByVehicleVin(String vehicleVin);

    boolean existsByVehicleVinAndIdNot(String vehicleVin, UUID id);

    boolean existsByCustomerUserId(UUID customerId);

    boolean existsByEmployeeUserId(UUID employeeId);
}

