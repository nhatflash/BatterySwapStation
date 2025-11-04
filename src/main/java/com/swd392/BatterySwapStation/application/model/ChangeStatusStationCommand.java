package com.swd392.BatterySwapStation.application.model;


import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeStatusStationCommand {
    private String stationId;
    private StationStatus newStatus;
}
