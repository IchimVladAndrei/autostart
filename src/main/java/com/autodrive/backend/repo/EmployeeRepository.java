package com.autodrive.backend.repo;

import com.autodrive.backend.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByCnp(String cnp);

    boolean existsByCnpAndUserIdNot(String cnp, UUID userId);
}
