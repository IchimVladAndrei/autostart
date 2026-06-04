package com.autodrive.backend.dto.vehicle;

public record BrandResponse(
        String name,
        String country,
        Integer foundedYear
) {
}
