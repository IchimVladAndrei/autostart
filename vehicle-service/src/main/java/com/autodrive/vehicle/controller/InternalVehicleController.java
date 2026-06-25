package com.autodrive.vehicle.controller;

import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.vehicle.dto.vehicle.VehicleStatusUpdateRequest;
import com.autodrive.vehicle.repo.VehicleRepository;
import com.autodrive.vehicle.service.InternalTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/vehicles")
public class InternalVehicleController {

    private final VehicleRepository vehicleRepository;
    private final InternalTokenService internalTokenService;

    @GetMapping("/{vin}/exists")
    public boolean vehicleExists(@PathVariable String vin, @RequestHeader(name = "X-Internal-Token", required = false) String token) {
        internalTokenService.validate(token);
        return vehicleRepository.existsById(vin);
    }

    @PatchMapping("/{vin}/status")
    @CacheEvict(cacheNames = "vehicle", allEntries = true)
    public void updateStatus(@PathVariable String vin,
                             @Valid @RequestBody VehicleStatusUpdateRequest request,
                             @RequestHeader(name = "X-Internal-Token", required = false) String token) {
        internalTokenService.validate(token);
        var vehicle = vehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + vin));
        vehicle.setStatus(request.status());
        vehicleRepository.save(vehicle);
    }
}
