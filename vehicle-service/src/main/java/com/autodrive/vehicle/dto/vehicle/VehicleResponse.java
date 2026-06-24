package com.autodrive.vehicle.dto.vehicle;

import com.autodrive.vehicle.entity.vehicle.VehicleStatus;

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