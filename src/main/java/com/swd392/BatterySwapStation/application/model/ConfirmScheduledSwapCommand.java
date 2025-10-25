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
public class ConfirmScheduledSwapCommand {
    private UUID transactionId;
    private UUID staffId;
    private List<UUID> batteryIds;
}
