package com.swd392.BatterySwapStation.presentation.dto;

import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStationRequest {
    String name;
    String address;
    @Min(value = 1, message = "Please enter the number of participants again!!! ")
    Integer totalCapacity;
    StationStatus status;
    @Min(value = 1, message = "Please enter the number of participants again!!! ")
    Integer totalSwapBays;
    String openingTime;
    String closingTime;
    String contactPhone;
    String contactEmail;
    String description;
    String imageUrl;
}
