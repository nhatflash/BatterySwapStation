package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.application.common.shared.SwappedBattery;
import jakarta.validation.constraints.NotBlank;
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
public class CreateScheduledBatterySwapRequest {
    private UUID vehicleId;

    @NotBlank(message = "Station Id is required.")
    private UUID stationId;

    private List<SwappedBattery> swappedBatteries;

    @NotBlank(message = "Scheduled time is required.")
    private String scheduledTime;
    private String notes;
}
