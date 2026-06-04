package com.autodrive.backend.mapper;

import com.autodrive.backend.dto.user.EmployeeCreateRequest;
import com.autodrive.backend.dto.user.EmployeeResponse;
import com.autodrive.backend.dto.user.EmployeeUpdateRequest;
import com.autodrive.backend.entity.user.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() {

    }

    public static Employee toEntity(EmployeeCreateRequest req) {
        return Employee
                .builder()
                .cnp(req.cnp())
                .name(req.name())
                .position(req.position())
                .status(req.status())
                .baseSalary(req.baseSalary())
                .hireDate(req.hireDate())
                .build();

    }

    public static EmployeeResponse toResponse(Employee e) {

        return new EmployeeResponse(e.getName(), e.getCnp(), e.getPosition(), e.getStatus(), e.getBaseSalary(), e.getHireDate(),e.getUserId());
    }

    public static void applyUpdates(Employee e, EmployeeUpdateRequest req) {
        if (req.name() != null) e.setName(req.name());
        if (req.cnp() != null) e.setCnp(req.cnp());
        if (req.position() != null) e.setPosition(req.position());
        if (req.status() != null) e.setStatus(req.status());
        if (req.baseSalary() != null) e.setBaseSalary(req.baseSalary());
        if (req.hireDate() != null) e.setHireDate(req.hireDate());
    }

}