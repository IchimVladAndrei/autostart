package com.autodrive.vehicle.controller;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.vehicle.dto.vehicle.BrandCreateRequest;
import com.autodrive.vehicle.dto.vehicle.BrandResponse;
import com.autodrive.vehicle.dto.vehicle.BrandUpdateRequest;
import com.autodrive.vehicle.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public PageResponse<BrandResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "name") String sortBy,
                                              @RequestParam(defaultValue = "asc") String direction) {
        return brandService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public BrandResponse getById(@PathVariable UUID id) {
        return brandService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public BrandResponse update(@PathVariable UUID id, @Valid @RequestBody BrandUpdateRequest request) {
        return brandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
