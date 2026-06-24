package com.autodrive.backend.controller;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.vehicle.VehicleCreateRequest;
import com.autodrive.backend.dto.vehicle.VehicleResponse;
import com.autodrive.backend.dto.vehicle.VehicleUpdateRequest;
import com.autodrive.backend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/cars", "/api/v1/vehicles"})
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public PageResponse<VehicleResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "vin") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return vehicleService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{vin}")
    public VehicleResponse getByVin(@PathVariable String vin) {
        return vehicleService.findById(vin);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleCreateRequest vehicle) {
        VehicleResponse created = vehicleService.create(vehicle);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{vin}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<VehicleResponse> update(@PathVariable String vin, @Valid @RequestBody VehicleUpdateRequest vehicle) {
        VehicleResponse updated = vehicleService.update(vin, vehicle);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{vin}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String vin) {
        vehicleService.delete(vin);
        return ResponseEntity
                .noContent()
                .build();
    }
}

