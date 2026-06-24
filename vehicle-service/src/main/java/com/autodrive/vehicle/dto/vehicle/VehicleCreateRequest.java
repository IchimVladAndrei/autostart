package com.autodrive.vehicle.dto.vehicle;

import com.autodrive.vehicle.entity.vehicle.VehicleStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VehicleCreateRequest(
        @NotBlank @Size(min = 17, max = 17) String vin,
        @NotBlank @Size(max = 100) String model,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal basePrice,
        @NotNull @Min(1900) Integer year,
        @NotNull VehicleStatus status,
        @NotBlank @Size(max = 50) String color,
        @NotNull @Min(0) Integer mileage,
        UUID brandId,
        List<UUID> extraOptionIds
) {
}