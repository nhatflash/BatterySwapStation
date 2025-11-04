package com.swd392.BatterySwapStation.application.model;

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
public class DefineBatteryModelCommand {
    private String type;
    private String manufacturer;
    private String chemistry;
    private int weightKg;
    private int warrantyMonths;
    private int maxChargePowerKwh;
    private BigDecimal minSohThreshold;
    private List<String> compatibleVehicles;
}
