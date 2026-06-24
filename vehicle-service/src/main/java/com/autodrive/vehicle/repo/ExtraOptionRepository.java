package com.autodrive.vehicle.repo;

import com.autodrive.vehicle.entity.vehicle.ExtraOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExtraOptionRepository extends JpaRepository<ExtraOption, UUID> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}

