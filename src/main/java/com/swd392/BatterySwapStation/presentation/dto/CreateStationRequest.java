package com.swd392.BatterySwapStation.presentation.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateStationRequest {

    @NotBlank(message = "Station name can not be null!!!")
    String name;

    @NotBlank(message = "Address station can not be null !!!")
    String address;

    @NotNull(message = "totalCapacity can not be null !!!")
    @Min(value = 1, message = "Please enter the number of participants again!!! ")
    Integer totalCapacity;

    @NotNull(message = "totalSwapBays can not be null !!!")
    @Min(value = 1, message = "Please enter the number of participants again!!! ")
    Integer totalSwapBays;

    @NotBlank(message = "openingTime can not be null!!!")
    String openingTime;

    @NotBlank(message = "closingTime can not be null!!!")
    String closingTime;

    @NotBlank(message = "contactPhone can not be null !!!")
    String contactPhone;

    @NotBlank(message = "contactEmail can not be null !!!")
    String contactEmail;

    @NotBlank(message = "description can not be null !!!")
    String description;

    @NotBlank(message = "imageUrl can not be null !!!")
    String imageUrl;
}
