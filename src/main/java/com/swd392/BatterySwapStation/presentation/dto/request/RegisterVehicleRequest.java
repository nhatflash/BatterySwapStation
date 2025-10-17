package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
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
    private Integer year;

    @NotBlank(message = "License plate is required.")
    private String licensePlate;

    @NotBlank(message = "Battery type is required.")
    private String batteryType;
}
