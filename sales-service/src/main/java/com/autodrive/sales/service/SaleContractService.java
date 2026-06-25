package com.autodrive.sales.service;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.sales.dto.sale.SaleContractCreateRequest;
import com.autodrive.sales.dto.sale.SaleContractResponse;
import com.autodrive.sales.dto.sale.SaleContractUpdateRequest;
import com.autodrive.sales.entity.sale.SaleContract;
import com.autodrive.sales.entity.sale.SaleContractStatus;
import com.autodrive.sales.client.AuthUserClient;
import com.autodrive.sales.client.VehicleClient;
import com.autodrive.common.exception.ConflictException;
import com.autodrive.common.exception.DuplicateResourceException;
import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.sales.mapper.SaleContractMapper;
import com.autodrive.sales.repo.PaymentRepository;
import com.autodrive.sales.repo.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final PaymentRepository paymentRepository;
    private final AuthUserClient authUserClient;
    private final VehicleClient vehicleClient;

    @Transactional(readOnly = true)
    public PageResponse<SaleContractResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(saleRepository.findAll(pageable).map(SaleContractMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<SaleContractResponse> findForCustomerEmail(String email, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return authUserClient
                .findCustomerIdByEmail(email)
                .map(customerId -> PageResponse.from(saleRepository.findByCustomerId(customerId, pageable).map(SaleContractMapper::toResponse)))
                .orElseGet(() -> PageResponse.from(Page.empty(pageable)));
    }

    @Transactional(readOnly = true)
    public SaleContractResponse findById(UUID id) {
        return SaleContractMapper.toResponse(getContract(id));
    }

    public SaleContractResponse create(SaleContractCreateRequest request) {
        if (saleRepository.existsByVehicleVin(request.vehicleVin())) {
            throw new DuplicateResourceException("Vehicle already has a sale contract: " + request.vehicleVin());
        }
        ensureCustomerExists(request.customerId());
        ensureEmployeeExists(request.employeeId());
        ensureVehicleExists(request.vehicleVin());

        SaleContract contract = SaleContractMapper.toEntity(request);
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

    private void ensureCustomerExists(UUID customerId) {
        if (!authUserClient.existsCustomer(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private void ensureEmployeeExists(UUID employeeId) {
        if (!authUserClient.existsEmployee(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
    }

    private void ensureVehicleExists(String vehicleVin) {
        if (!vehicleClient.existsVehicle(vehicleVin)) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vehicleVin);
        }
    }

    private void updateVehicleStatusForCompletedContract(SaleContract contract) {
        if (contract.getStatus() == SaleContractStatus.COMPLETED && contract.getVehicleVin() != null) {
            vehicleClient.updateVehicleStatus(contract.getVehicleVin(), "SOLD");
        }
    }
}
