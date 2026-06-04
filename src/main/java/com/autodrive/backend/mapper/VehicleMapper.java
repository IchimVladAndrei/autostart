package com.autodrive.backend.mapper;

import com.autodrive.backend.dto.vehicle.*;
import com.autodrive.backend.entity.vehicle.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

public final class VehicleMapper {

    private VehicleMapper() {

    }


    public static Vehicle toEntity(VehicleCreateRequest req) {
        return Vehicle
                .builder()
                .vin(req.vin())
                .model(req.model())
                .basePrice(req.basePrice())
                .year(req.year())
                .status(req.status())
                .color(req.color())
                .mileage(req.mileage())
                .build();
    }

    public static VehicleResponse toResponse(Vehicle v) {
        BrandSummaryResponse brandResp = null;
        if (v.getBrand() != null) {
            brandResp = new BrandSummaryResponse(v
                    .getBrand()
                    .getId(), v
                    .getBrand()
                    .getName());
        }

        List<ExtraOptionSummaryResponse> options = List.of();
        if (v.getExtraOptions() != null && !v
                .getExtraOptions()
                .isEmpty()) {
            options = v
                    .getExtraOptions()
                    .stream()
                    .map(opt -> new ExtraOptionSummaryResponse(opt.getId(), opt.getName(), opt.getPrice()))
                    .collect(Collectors.toList());
        }

        return new VehicleResponse(
                v.getVin(),
                v.getModel(),
                v.getBasePrice(),
                v.getYear(),
                v.getStatus(),
                v.getColor(),
                v.getMileage(),
                brandResp,
                options
        );
    }


    public static void applyUpdate(Vehicle entity, VehicleUpdateRequest req) {
        if (req.model() != null) entity.setModel(req.model());
        if (req.basePrice() != null) entity.setBasePrice(req.basePrice());
        if (req.year() != null) entity.setYear(req.year());
        if (req.status() != null) entity.setStatus(req.status());
        if (req.color() != null) entity.setColor(req.color());
        if (req.mileage() != null) entity.setMileage(req.mileage());
    }
}
