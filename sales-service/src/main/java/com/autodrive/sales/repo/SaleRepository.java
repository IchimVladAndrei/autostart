package com.autodrive.sales.repo;

import com.autodrive.sales.entity.sale.SaleContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleContract, UUID> {
    boolean existsByVehicleVin(String vehicleVin);

    boolean existsByVehicleVinAndIdNot(String vehicleVin, UUID id);

    boolean existsByCustomerId(UUID customerId);

    boolean existsByEmployeeId(UUID employeeId);
}

