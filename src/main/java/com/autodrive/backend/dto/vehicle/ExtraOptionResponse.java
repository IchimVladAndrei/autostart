package com.autodrive.backend.dto.vehicle;

import java.math.BigDecimal;

public record ExtraOptionResponse(String name, String description, BigDecimal price) {
}
