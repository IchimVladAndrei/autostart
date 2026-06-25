package com.autodrive.authuser.repo;

import com.autodrive.authuser.entity.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByCnp(String cnp);

    boolean existsByCnpAndUserIdNot(String cnp, UUID userId);

    Optional<Customer> findByUserEmailIgnoreCase(String email);
}

