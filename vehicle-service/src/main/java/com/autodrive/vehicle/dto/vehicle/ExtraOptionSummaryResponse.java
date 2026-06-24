package com.autodrive.vehicle.dto.vehicle;

import java.math.BigDecimal;
import java.util.UUID;

public record ExtraOptionSummaryResponse(UUID id, String name, BigDecimal price) {
}