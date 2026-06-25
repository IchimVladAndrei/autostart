package com.autodrive.sales.controller;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.sales.dto.sale.SaleContractCreateRequest;
import com.autodrive.sales.dto.sale.SaleContractResponse;
import com.autodrive.sales.dto.sale.SaleContractUpdateRequest;
import com.autodrive.sales.service.SaleContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sale-contracts")
public class SaleContractController {
    private final SaleContractService saleContractService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_SALES_CONSULTANT')")
    public PageResponse<SaleContractResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "contractDate") String sortBy,
                                                     @RequestParam(defaultValue = "desc") String direction) {
        return saleContractService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') and !hasAnyAuthority('POSITION_MANAGER','POSITION_SALES_CONSULTANT','POSITION_SERVICE_ADVISOR','POSITION_MECHANIC','POSITION_FINANCE_SPECIALIST','POSITION_ADMINISTRATOR')")
    public PageResponse<SaleContractResponse> getMine(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "contractDate") String sortBy,
                                                      @RequestParam(defaultValue = "desc") String direction,
                                                      Authentication authentication) {
        return saleContractService.findForCustomerEmail(authentication.getName(), page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_SALES_CONSULTANT')")
    public SaleContractResponse getById(@PathVariable UUID id) {
        return saleContractService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_SALES_CONSULTANT')")
    public ResponseEntity<SaleContractResponse> create(@Valid @RequestBody SaleContractCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleContractService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER') or hasAuthority('POSITION_SALES_CONSULTANT')")
    public SaleContractResponse update(@PathVariable UUID id, @Valid @RequestBody SaleContractUpdateRequest request) {
        return saleContractService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        saleContractService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
