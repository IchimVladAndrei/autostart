package com.autodrive.backend.dto.vehicle;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ExtraOptionUpdateRequest(
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be strictly positive")
        BigDecimal price) {
}
