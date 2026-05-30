package com.autodrive.backend.controller;

import com.autodrive.backend.entity.car.Vehicle;
import com.autodrive.backend.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private CarController carController;

    @Test
    void shouldReturnCars() {
        when(vehicleService.findAll()).thenReturn(List.of());

        assertEquals(0, carController.getAll().size());
    }

    @Test
    void shouldCreateCar() {
        Vehicle vehicle = Vehicle
                .builder()
                .id(UUID.randomUUID())
                .vin("WVWZZZ1JZXW000001")
                .model("Golf")
                .price(new BigDecimal("10000.00"))
                .build();

        when(vehicleService.create(any(Vehicle.class))).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = carController.create(vehicle);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(vehicleService).create(any(Vehicle.class));
    }
}
