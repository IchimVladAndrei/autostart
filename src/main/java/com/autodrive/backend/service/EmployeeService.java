package com.autodrive.backend.service;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.user.EmployeeCreateRequest;
import com.autodrive.backend.dto.user.EmployeeResponse;
import com.autodrive.backend.dto.user.EmployeeUpdateRequest;
import com.autodrive.backend.entity.user.Employee;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.DuplicateResourceException;
import com.autodrive.backend.exception.ResourceNotFoundException;
import com.autodrive.backend.mapper.EmployeeMapper;
import com.autodrive.backend.repo.EmployeeRepository;
import com.autodrive.backend.repo.SaleRepository;
import com.autodrive.backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {
    private static final Set<String> SORTS = Set.of("userId", "name", "cnp", "position", "status", "baseSalary", "bonus", "hireDate");

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(employeeRepository.findAll(pageable).map(EmployeeMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(UUID id) {
        return EmployeeMapper.toResponse(getEmployee(id));
    }

    public EmployeeResponse create(EmployeeCreateRequest request) {
        if (employeeRepository.existsById(request.userId())) {
            throw new ConflictException("User already has an employee profile");
        }
        if (employeeRepository.existsByCnp(request.cnp())) {
            throw new DuplicateResourceException("Employee already exists with CNP: " + request.cnp());
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        Employee employee = EmployeeMapper.toEntity(request);
        employee.setUser(user);
        user.setEmployee(employee);
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse update(UUID id, EmployeeUpdateRequest request) {
        Employee employee = getEmployee(id);
        if (request.cnp() != null && employeeRepository.existsByCnpAndUserIdNot(request.cnp(), id)) {
            throw new DuplicateResourceException("Employee already exists with CNP: " + request.cnp());
        }
        EmployeeMapper.applyUpdates(employee, request);
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    public void delete(UUID id) {
        Employee employee = getEmployee(id);
        if (saleRepository.existsByEmployeeId(id)) {
            throw new ConflictException("Cannot delete employee referenced by sale contracts");
        }
        if (employee.getUser() != null) {
            employee.getUser().setEmployee(null);
        }
        employeeRepository.delete(employee);
    }

    private Employee getEmployee(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
