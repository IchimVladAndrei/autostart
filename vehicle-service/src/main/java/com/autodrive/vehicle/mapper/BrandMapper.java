package com.autodrive.vehicle.mapper;

import com.autodrive.vehicle.dto.vehicle.BrandCreateRequest;
import com.autodrive.vehicle.dto.vehicle.BrandResponse;
import com.autodrive.vehicle.dto.vehicle.BrandUpdateRequest;
import com.autodrive.vehicle.entity.vehicle.Brand;

public final class BrandMapper {

    private BrandMapper() {

    }

    public static Brand toEntity(BrandCreateRequest req) {

        return Brand
                .builder()
                .name(req.name())
                .country(req.country())
                .foundedYear(req.foundedYear())
                .build();
    }

    public static BrandResponse toResponse(Brand b) {
        return new BrandResponse(
                b.getName(),
                b.getCountry(),
                b.getFoundedYear(),
                b.getId()
        );
    }

    public static void applyUpdate(Brand entity, BrandUpdateRequest req) {
        if (req.name() != null) entity.setName(req.name());
        if (req.country() != null) entity.setCountry(req.country());
        if (req.foundedYear() != null) entity.setFoundedYear(req.foundedYear());
    }
}
