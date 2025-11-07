package com.swd392.BatterySwapStation.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefineBatteryModelRequest {
    @NotBlank(message = "Battery type is required.")
    private String type;

    @NotBlank(message = "Manufacturer is required.")
    private String manufacturer;

    @NotBlank(message = "Chemistry is required.")
    private String chemistry;

    @NotNull(message = "Battery weight is required.")
    @Min(value = 0, message = "Battery weight cannot be lower than 0.")
    private int weightKg;

    @Min(value = 0, message = "Battery warranty months cannot be lower than 0.")
    private int warrantyMonths;

    @Min(value = 0, message = "Battery max charge power cannot be lower than 0.")
    private int maxChargePowerKwh;

    private BigDecimal minSohThreshold;
    private List<String> compatibleVehicles;
}
