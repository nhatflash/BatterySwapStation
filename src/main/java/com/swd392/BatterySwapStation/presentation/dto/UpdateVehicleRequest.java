package com.swd392.BatterySwapStation.presentation.dto;


import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {
    private String vin;
    private String make;
    private String model;
    @Min(value = 2000, message = "Year must be greater than 2000.")
    private Integer year;
    private String licensePlate;
    private String batteryType;
    @Min(value = 1, message = "Battery capacity must be greater than 0.")
    private Integer batteryCapacity;
}
