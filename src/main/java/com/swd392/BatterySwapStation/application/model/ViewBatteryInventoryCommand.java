package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewBatteryInventoryCommand {
    private UUID staffId;
    private BatteryStatus batteryStatus;
    private Integer pageIndex;
}
