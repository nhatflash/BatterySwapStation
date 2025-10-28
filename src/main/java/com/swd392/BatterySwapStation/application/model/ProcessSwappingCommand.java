package com.swd392.BatterySwapStation.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSwappingCommand {
    private UUID staffId;
    private UUID transactionId;
    private boolean isProcessing;
}
