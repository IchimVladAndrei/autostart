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

import java.util.List;
import java.util.UUID;

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
    public Vehicle findById(UUID id) {
        return vehicleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + id));
    }

    public Vehicle create(Vehicle vehicle) {
        applyRelations(vehicle);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(UUID id, Vehicle vehicle) {
        Vehicle existing = findById(id);
        existing.setVin(vehicle.getVin());
        existing.setModel(vehicle.getModel());
        existing.setBrand(vehicle.getBrand());
        existing.setExtraOptions(vehicle.getExtraOptions());
        applyRelations(existing);
        return vehicleRepository.save(existing);
    }

    public void delete(UUID id) {
        if (!vehicleRepository.existsById(id)) {
            throw new EntityNotFoundException("Car not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private void applyRelations(Vehicle vehicle) {
        if (vehicle.getBrand() != null && vehicle
                .getBrand()
                .getId() != null) {
            Brand brand = brandRepository
                    .findById(vehicle
                            .getBrand()
                            .getId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + vehicle
                            .getBrand()
                            .getId()));
            vehicle.setBrand(brand);
        }

        if (vehicle.getExtraOptions() != null && !vehicle
                .getExtraOptions()
                .isEmpty()) {
            List<UUID> optionIds = vehicle
                    .getExtraOptions()
                    .stream()
                    .map(ExtraOption::getId)
                    .filter(id -> id != null)
                    .toList();
            if (!optionIds.isEmpty()) {
                List<ExtraOption> options = extraOptionRepository.findAllById(optionIds);
                if (options.size() != optionIds.size()) {
                    throw new EntityNotFoundException("One or more extra options were not found");
                }
                vehicle.setExtraOptions(options);
            }
        }
    }
}
