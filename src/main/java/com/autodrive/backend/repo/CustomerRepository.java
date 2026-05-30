package com.autodrive.backend.repo;

import com.autodrive.backend.entity.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}

