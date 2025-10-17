package com.swd392.BatterySwapStation.application.model;


import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class ChangeStatusStationCommand {
    private String stationId;
    private StationStatus newStatus;
}
