package com.autodrive.backend.repo;

import com.autodrive.backend.entity.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
}

