package com.autodrive.backend.mapper;

import com.autodrive.backend.dto.vehicle.ExtraOptionCreateRequest;
import com.autodrive.backend.dto.vehicle.ExtraOptionResponse;
import com.autodrive.backend.dto.vehicle.ExtraOptionUpdateRequest;
import com.autodrive.backend.entity.vehicle.ExtraOption;

public final class ExtraOptionMapper {

    private ExtraOptionMapper() {

    }

    public static ExtraOption toEntity(ExtraOptionCreateRequest req) {
        return ExtraOption
                .builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .build();
    }

    public static ExtraOptionResponse toResponse(ExtraOption eopt) {
        return new ExtraOptionResponse(
                eopt.getName(),
                eopt.getDescription(),
                eopt.getPrice(),
                eopt.getId()
        );
    }

    public static void applyUpdate(ExtraOption entity, ExtraOptionUpdateRequest req) {
        if (req.name() != null) entity.setName(req.name());
        if (req.description() != null) entity.setDescription(req.description());
        if (req.price() != null) entity.setPrice(req.price());
    }


}
