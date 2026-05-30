package com.autodrive.backend.service;

import com.autodrive.backend.entity.car.Brand;
import com.autodrive.backend.entity.car.ExtraOption;
import com.autodrive.backend.entity.car.Vehicle;
import com.autodrive.backend.repo.BrandRepository;
import com.autodrive.backend.repo.ExtraOptionRepository;
import com.autodrive.backend.repo.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BrandRepository brandRepository;
    private final ExtraOptionRepository extraOptionRepository;

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(String vin) {
        return vehicleRepository
                .findById(vin)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with VIN: " + vin));
    }

    public Vehicle create(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        if (vehicle.getVin() == null || vehicle
                .getVin()
                .isBlank()) {
            throw new IllegalArgumentException("VIN is required");
        }
        if (vehicleRepository.existsById(vehicle.getVin())) {
            throw new IllegalArgumentException("Vehicle with VIN already exists: " + vehicle.getVin());
        }

        applyRelations(vehicle);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(String vin, Vehicle vehicle) {
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("VIN path variable is required");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle payload must not be null");
        }

        Vehicle existing = findById(vin);


        if (vehicle.getModel() != null) existing.setModel(vehicle.getModel());
        if (vehicle.getBasePrice() != null) existing.setBasePrice(vehicle.getBasePrice());
        if (vehicle.getYear() != null) existing.setYear(vehicle.getYear());
        if (vehicle.getStatus() != null) existing.setStatus(vehicle.getStatus());
        if (vehicle.getColor() != null) existing.setColor(vehicle.getColor());
        if (vehicle.getMileage() != null) existing.setMileage(vehicle.getMileage());


        if (vehicle.getBrand() != null) {
            existing.setBrand(vehicle.getBrand());
        }
        if (vehicle.getExtraOptions() != null) {
            existing.setExtraOptions(vehicle.getExtraOptions());
        }

        applyRelations(existing);
        return vehicleRepository.save(existing);
    }

    public void delete(String vin) {
        if (!vehicleRepository.existsById(vin)) {
            throw new EntityNotFoundException("Vehicle not found with VIN: " + vin);
        }
        vehicleRepository.deleteById(vin);
    }

    private void applyRelations(Vehicle vehicle) {

        if (vehicle.getBrand() != null && vehicle
                .getBrand()
                .getId() != null) {
            UUID brandId = vehicle
                    .getBrand()
                    .getId();
            Brand brand = brandRepository
                    .findById(brandId)
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + brandId));
            vehicle.setBrand(brand);
        }


        if (vehicle.getExtraOptions() != null && !vehicle
                .getExtraOptions()
                .isEmpty()) {
            List<UUID> optionIds = vehicle
                    .getExtraOptions()
                    .stream()
                    .map(ExtraOption::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!optionIds.isEmpty()) {
                Set<ExtraOption> found = new HashSet<>(extraOptionRepository.findAllById(optionIds));
                if (found.size() != optionIds.size()) {
                    throw new EntityNotFoundException("One or more extra options were not found");
                }
                vehicle.setExtraOptions(found);
            }
        }
    }
}