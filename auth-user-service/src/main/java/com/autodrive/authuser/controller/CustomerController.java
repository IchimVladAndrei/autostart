package com.autodrive.authuser.controller;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.authuser.dto.user.CustomerCreateRequest;
import com.autodrive.authuser.dto.user.CustomerResponse;
import com.autodrive.authuser.dto.user.CustomerUpdateRequest;
import com.autodrive.authuser.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public PageResponse<CustomerResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "name") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String direction) {
        return customerService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable UUID id) {
        return customerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody CustomerUpdateRequest request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
