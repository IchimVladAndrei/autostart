package com.autodrive.backend.repo;

import com.autodrive.backend.entity.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}

