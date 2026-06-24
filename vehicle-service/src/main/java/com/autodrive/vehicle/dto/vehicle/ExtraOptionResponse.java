package com.autodrive.vehicle.dto.vehicle;

import java.math.BigDecimal;
import java.util.UUID;

public record ExtraOptionResponse(String name, String description, BigDecimal price, UUID id) {
}
