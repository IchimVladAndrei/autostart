package com.autodrive.backend.mapper;

import com.autodrive.backend.dto.vehicle.BrandCreateRequest;
import com.autodrive.backend.dto.vehicle.BrandResponse;
import com.autodrive.backend.dto.vehicle.BrandUpdateRequest;
import com.autodrive.backend.entity.vehicle.Brand;

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

    public static BrandResponse toResponse(Brand brand) {
        return new BrandResponse(
                brand.getName(),
                brand.getCountry(),
                brand.getFoundedYear()
        );
    }

    public static void applyUpdate(Brand entity, BrandUpdateRequest req) {
        if (req.name() != null) entity.setName(req.name());
        if (req.country() != null) entity.setCountry(req.country());
        if (req.foundedYear() != null) entity.setFoundedYear(req.foundedYear());
    }
}
