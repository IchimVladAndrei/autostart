package com.autodrive.vehicle.dto.vehicle;

import com.autodrive.vehicle.entity.vehicle.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VehicleUpdateRequest(
        @Size(max = 100)
        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String model,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal basePrice,
        @Min(1900) Integer year,
        VehicleStatus status,
        @Size(max = 50)
        @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
        String color,
        @Min(0) Integer mileage,
        UUID brandId,
        List<UUID> extraOptionIds
) {
}
