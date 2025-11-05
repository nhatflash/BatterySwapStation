package com.swd392.BatterySwapStation.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryTransactionResponse {
    private UUID oldBatteryId;
    private UUID newBatteryId;
}
