package com.swd392.BatterySwapStation.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private int weightKg;
    private int warrantyMonths;
    private int maxChargePowerKwh;
    private BigDecimal minSohThreshold;
}
