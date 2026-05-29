package com.autodrive.backend.controller;

import com.autodrive.backend.entity.car.Car;
import com.autodrive.backend.service.CarService;
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
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<Car> getAll() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public Car getById(@PathVariable UUID id) {
        return carService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Car> create(@Valid @RequestBody Car car) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.create(car));
    }

    @PutMapping("/{id}")
    public Car update(@PathVariable UUID id, @Valid @RequestBody Car car) {
        return carService.update(id, car);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

