package com.autodrive.backend.repo;

import com.autodrive.backend.entity.car.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
}

