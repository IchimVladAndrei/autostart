package com.autodrive.backend.repo;

import com.autodrive.backend.entity.car.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
}

