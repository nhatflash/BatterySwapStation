package com.swd392.BatterySwapStation.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalkInSwapRequest {
    @NotNull(message = "Driver Id is required.")
    private UUID driverId;

    @NotNull(message = "Vehicle Id is required.")
    private UUID vehicleId;

    @NotNull(message = "Battery Ids are required.")
    private List<UUID> batteryIds;

    private String notes;
}
