package com.swd392.BatterySwapStation.application.model;

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
public class DefineBatteryModelCommand {
    private String type;
    private String manufacturer;
    private String chemistry;
    private int weightKg;
    private int warrantyMonths;
    private int maxChargePowerKwh;
    private BigDecimal minSohThreshold;
}
