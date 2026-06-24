package com.autodrive.backend.controller;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.vehicle.VehicleCreateRequest;
import com.autodrive.backend.dto.vehicle.VehicleResponse;
import com.autodrive.backend.entity.vehicle.VehicleStatus;
import com.autodrive.backend.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void shouldReturnCars() {
        when(vehicleService.findAll(0, 10, "vin", "asc")).thenReturn(new PageResponse<>(
                java.util.List.of(),
                0,
                10,
                0,
                0,
                true,
                true
        ));

        assertEquals(0, vehicleController
                .getAll(0, 10, "vin", "asc")
                .content()
                .size());
    }

    @Test
    void shouldCreateCar() {
        VehicleCreateRequest request = new VehicleCreateRequest(
                "WVWZZZ1JZXW000001",
                "Golf",
                new BigDecimal("10000.00"),
                2020,
                VehicleStatus.AVAILABLE,
                "Black",
                0,
                null,
                java.util.List.of()
        );
        VehicleResponse created = new VehicleResponse(
                request.vin(),
                request.model(),
                request.basePrice(),
                request.year(),
                request.status(),
                request.color(),
                request.mileage(),
                null,
                java.util.List.of()
        );

        when(vehicleService.create(any(VehicleCreateRequest.class))).thenReturn(created);

        ResponseEntity<VehicleResponse> response = vehicleController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(vehicleService).create(any(VehicleCreateRequest.class));
    }
}
