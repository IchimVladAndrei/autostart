package com.autodrive.backend.dto.vehicle;

import com.autodrive.backend.entity.vehicle.VehicleStatus;

import java.math.BigDecimal;
import java.util.List;

public record VehicleResponse(
        String vin,
        String model,
        BigDecimal basePrice,
        Integer year,
        VehicleStatus status,
        String color,
        Integer mileage,
        BrandSummaryResponse brand,
        List<ExtraOptionSummaryResponse> extraOptions
) {
}