package com.autodrive.authuser.repo;

import com.autodrive.authuser.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = "employee")
    Optional<User> findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    @EntityGraph(attributePaths = {"employee", "customer"})
    Optional<User> findWithEmployeeAndCustomerById(UUID id);
}
