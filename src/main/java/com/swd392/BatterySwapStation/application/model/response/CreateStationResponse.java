package com.swd392.BatterySwapStation.application.model.response;

import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateStationResponse {
    String stationId;
    String name;
    String address;
    Integer totalCapacity;
    Integer currentCapacity;
    Integer idleSwapBays;
    Integer totalSwapBays;
    StationStatus status;
    String openingTime;
    String closingTime;
    String contactPhone;
    String contactEmail;
    String description;
    String imageUrl;
}
