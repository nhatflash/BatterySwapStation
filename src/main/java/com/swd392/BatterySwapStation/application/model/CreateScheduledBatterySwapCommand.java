package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.application.common.shared.SwappedBattery;
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
public class CreateScheduledBatterySwapCommand {
    private UUID driverId;
    private UUID vehicleId;
    private UUID stationId;
    private List<SwappedBattery> swappedBatteries;
    private String scheduledTime;
    private String notes;
}
