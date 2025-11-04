package com.swd392.BatterySwapStation.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduledBatterySwapRequest {
    @NotNull(message = "Vehicle Id is required.")
    private UUID vehicleId;

    @NotNull(message = "Station Id is required.")
    private UUID stationId;

    @NotBlank(message = "Scheduled time is required.")
    private String scheduledTime;
    private String notes;
}
