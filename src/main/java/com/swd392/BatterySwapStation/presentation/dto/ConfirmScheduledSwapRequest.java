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
public class ConfirmScheduledSwapRequest {

    @NotNull(message = "Battery Ids are required.")
    private List<UUID> batteryIds;
}
