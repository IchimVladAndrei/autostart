package com.autodrive.backend.repo;

import com.autodrive.backend.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    @Override
    @EntityGraph(attributePaths = {"brand", "extraOptions"})
    Page<Vehicle> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"brand", "extraOptions"})
    Optional<Vehicle> findById(String vin);

    boolean existsByBrandId(java.util.UUID brandId);

    boolean existsByExtraOptionsId(java.util.UUID optionId);
}

