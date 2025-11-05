package com.swd392.BatterySwapStation.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduledBatterySwapCommand {
    private UUID vehicleId;
    private UUID stationId;
    private String scheduledTime;
    private String notes;
}
