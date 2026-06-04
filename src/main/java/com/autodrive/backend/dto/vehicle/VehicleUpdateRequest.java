package com.autodrive.backend.dto.vehicle;

import com.autodrive.backend.entity.vehicle.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VehicleUpdateRequest(
        @Size(max = 100) String model,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal basePrice,
        @Min(1900) Integer year,
        VehicleStatus status,
        @Size(max = 50) String color,
        @Min(0) Integer mileage,
        UUID brandId,
        List<UUID> extraOptionIds
) {
}