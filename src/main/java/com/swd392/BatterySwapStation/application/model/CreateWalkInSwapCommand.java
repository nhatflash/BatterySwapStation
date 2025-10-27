package com.swd392.BatterySwapStation.application.model;

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
public class CreateWalkInSwapCommand {
    private UUID staffId;
    private UUID driverId;
    private UUID vehicleId;
    private List<UUID> batteryIds;
    private String notes;
}
