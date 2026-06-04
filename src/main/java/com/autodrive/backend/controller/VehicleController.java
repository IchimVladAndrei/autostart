package com.autodrive.backend.controller;

import com.autodrive.backend.dto.vehicle.VehicleCreateRequest;
import com.autodrive.backend.dto.vehicle.VehicleResponse;
import com.autodrive.backend.dto.vehicle.VehicleUpdateRequest;
import com.autodrive.backend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<VehicleResponse> getAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{vin}")
    public VehicleResponse getByVin(@PathVariable String vin) {
        return vehicleService.findById(vin);
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleCreateRequest vehicle) {
        VehicleResponse created = vehicleService.create(vehicle);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{vin}")
    public ResponseEntity<VehicleResponse> update(@PathVariable String vin, @Valid @RequestBody VehicleUpdateRequest vehicle) {
        VehicleResponse updated = vehicleService.update(vin, vehicle);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<Void> delete(@PathVariable String vin) {
        vehicleService.delete(vin);
        return ResponseEntity
                .noContent()
                .build();
    }
}

