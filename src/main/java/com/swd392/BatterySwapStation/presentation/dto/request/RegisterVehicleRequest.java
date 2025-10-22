package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVehicleRequest {
    @NotBlank(message = "VIN is required.")
    private String vin;

    @NotBlank(message = "Make is required.")
    private String make;

    @NotBlank(message = "Model is required.")
    private String model;

    @Min(value = 2000, message = "Year must be greater than 2000.")
    private Integer year;

    @NotBlank(message = "License plate is required.")
    private String licensePlate;

    @NotBlank(message = "Battery type is required.")
    private String batteryType;

    @NotBlank(message = "Battery capacity is required.")
    @Min(value = 1, message = "Battery capacity must be greater than 0.")
    private Integer batteryCapacity;
}
