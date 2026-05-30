package com.autodrive.backend.controller;

import com.autodrive.backend.entity.car.Vehicle;
import com.autodrive.backend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{vin}")
    public Vehicle getByVin(@PathVariable String vin) {
        return vehicleService.findById(vin);
    }

    @PostMapping
    public ResponseEntity<Vehicle> create(@Valid @RequestBody Vehicle vehicle) {
        Vehicle created = vehicleService.create(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{vin}")
    public Vehicle update(@PathVariable String vin, @Valid @RequestBody Vehicle vehicle) {
        return vehicleService.update(vin, vehicle);
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<Void> delete(@PathVariable String vin) {
        vehicleService.delete(vin);
        return ResponseEntity.noContent().build();
    }
}

