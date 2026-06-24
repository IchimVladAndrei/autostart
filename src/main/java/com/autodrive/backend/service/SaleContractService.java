package com.autodrive.backend.service;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.sale.SaleContractCreateRequest;
import com.autodrive.backend.dto.sale.SaleContractResponse;
import com.autodrive.backend.dto.sale.SaleContractUpdateRequest;
import com.autodrive.backend.entity.sale.SaleContract;
import com.autodrive.backend.entity.sale.SaleContractStatus;
import com.autodrive.backend.entity.user.Customer;
import com.autodrive.backend.entity.user.Employee;
import com.autodrive.backend.entity.vehicle.Vehicle;
import com.autodrive.backend.entity.vehicle.VehicleStatus;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.DuplicateResourceException;
import com.autodrive.backend.exception.ResourceNotFoundException;
import com.autodrive.backend.mapper.SaleContractMapper;
import com.autodrive.backend.repo.CustomerRepository;
import com.autodrive.backend.repo.EmployeeRepository;
import com.autodrive.backend.repo.PaymentRepository;
import com.autodrive.backend.repo.SaleRepository;
import com.autodrive.backend.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleContractService {
    private static final Set<String> SORTS = Set.of("id", "contractDate", "salePrice", "status");

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public PageResponse<SaleContractResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(saleRepository.findAll(pageable).map(SaleContractMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public SaleContractResponse findById(UUID id) {
        return SaleContractMapper.toResponse(getContract(id));
    }

    public SaleContractResponse create(SaleContractCreateRequest request) {
        if (saleRepository.existsByVehicleVin(request.vehicleVin())) {
            throw new DuplicateResourceException("Vehicle already has a sale contract: " + request.vehicleVin());
        }
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.employeeId()));
        Vehicle vehicle = vehicleRepository.findById(request.vehicleVin())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.vehicleVin()));

        SaleContract contract = SaleContractMapper.toEntity(request);
        contract.setCustomer(customer);
        contract.setEmployee(employee);
        contract.setVehicle(vehicle);
        updateVehicleStatusForCompletedContract(contract);
        return SaleContractMapper.toResponse(saleRepository.save(contract));
    }

    public SaleContractResponse update(UUID id, SaleContractUpdateRequest request) {
        SaleContract contract = getContract(id);
        SaleContractMapper.applyUpdates(contract, request);
        updateVehicleStatusForCompletedContract(contract);
        return SaleContractMapper.toResponse(saleRepository.save(contract));
    }

    public void delete(UUID id) {
        SaleContract contract = getContract(id);
        if (paymentRepository.existsByContractId(id)) {
            throw new ConflictException("Cannot delete sale contract with payments");
        }
        saleRepository.delete(contract);
    }

    private SaleContract getContract(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale contract not found with id: " + id));
    }

    private void updateVehicleStatusForCompletedContract(SaleContract contract) {
        if (contract.getStatus() == SaleContractStatus.COMPLETED && contract.getVehicle() != null) {
            contract.getVehicle().setStatus(VehicleStatus.SOLD);
        }
    }
}
