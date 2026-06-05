package com.autodrive.backend.dto.vehicle;

import java.util.UUID;

public record BrandResponse(
        String name,
        String country,
        Integer foundedYear, UUID id
) {
}
