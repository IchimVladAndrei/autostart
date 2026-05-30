package com.autodrive.backend.repo;

import com.autodrive.backend.entity.sale.SaleContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleContract, UUID> {
}

