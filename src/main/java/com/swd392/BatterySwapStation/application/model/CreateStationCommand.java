package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateStationCommand {
    String name;
    String address;
    Integer totalCapacity;
    Integer totalSwapBays;
    StationStatus status;
    String openingTime;
    String closingTime;
    String contactPhone;
    String contactEmail;
    String description;
    String imageUrl;
}
