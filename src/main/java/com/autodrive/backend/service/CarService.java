package com.autodrive.backend.service;

import com.autodrive.backend.entity.car.Brand;
import com.autodrive.backend.entity.car.Car;
import com.autodrive.backend.entity.car.ExtraOption;
import com.autodrive.backend.exception.CarNotFoundException;
import com.autodrive.backend.repo.BrandRepository;
import com.autodrive.backend.repo.CarRepository;
import com.autodrive.backend.repo.ExtraOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {

    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final ExtraOptionRepository extraOptionRepository;

    @Transactional(readOnly = true)
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Car findById(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));
    }

    public Car create(Car car) {
        applyRelations(car);
        car.setId(null);
        return carRepository.save(car);
    }

    public Car update(UUID id, Car car) {
        Car existing = findById(id);
        existing.setVin(car.getVin());
        existing.setModel(car.getModel());
        existing.setPrice(car.getPrice());
        existing.setBrand(car.getBrand());
        existing.setExtraOptions(car.getExtraOptions());
        applyRelations(existing);
        return carRepository.save(existing);
    }

    public void delete(UUID id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
    }

    private void applyRelations(Car car) {
        if (car.getBrand() != null && car.getBrand().getId() != null) {
            Brand brand = brandRepository.findById(car.getBrand().getId())
                    .orElseThrow(() -> new CarNotFoundException("Brand not found with id: " + car.getBrand().getId()));
            car.setBrand(brand);
        }

        if (car.getExtraOptions() != null && !car.getExtraOptions().isEmpty()) {
            List<UUID> optionIds = car.getExtraOptions().stream()
                    .map(ExtraOption::getId)
                    .filter(id -> id != null)
                    .toList();
            if (!optionIds.isEmpty()) {
                List<ExtraOption> options = extraOptionRepository.findAllById(optionIds);
                if (options.size() != optionIds.size()) {
                    throw new EntityNotFoundException("One or more extra options were not found");
                }
                car.setExtraOptions(options);
            }
        }
    }
}
