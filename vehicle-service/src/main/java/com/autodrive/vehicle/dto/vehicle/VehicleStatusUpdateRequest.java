package com.autodrive.vehicle.dto.vehicle;

import com.autodrive.vehicle.entity.vehicle.VehicleStatus;
import jakarta.validation.constraints.NotNull;

public record VehicleStatusUpdateRequest(@NotNull VehicleStatus status) {
}
