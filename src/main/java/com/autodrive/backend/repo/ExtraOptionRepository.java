package com.autodrive.backend.repo;

import com.autodrive.backend.entity.car.ExtraOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExtraOptionRepository extends JpaRepository<ExtraOption, UUID> {
}

