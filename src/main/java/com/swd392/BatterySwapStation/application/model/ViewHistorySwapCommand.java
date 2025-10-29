package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ViewHistorySwapCommand {
    private UUID driverId;
    private TransactionStatus status;
}
