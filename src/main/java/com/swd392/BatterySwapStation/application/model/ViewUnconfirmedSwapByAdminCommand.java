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
public class ViewUnconfirmedSwapByAdminCommand {
    private UUID adminId;
    private UUID stationId;
}
