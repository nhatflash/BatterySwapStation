package com.swd392.BatterySwapStation.presentation.dto.response;


import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChangeStationStatusResponse {
    private UUID stationId;
    private StationStatus newStatus;
}
