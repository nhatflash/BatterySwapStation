package com.swd392.BatterySwapStation.application.model.command;

import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStationCommand {
    private UUID stationId;
    private String name;
    private String address;
    private Integer totalCapacity;
    private Integer totalSwapBays;
    private StationStatus status;
    private String openingTime;
    private String closingTime;
    private String contactPhone;
    private String contactEmail;
    private String description;
    private String imageUrl;
}
