package com.swd392.BatterySwapStation.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBatteryModelRequest {
    private String type;
    private String manufacturer;
    private String chemistry;
    private int weightKg;
    private int warrantyMonths;
    private int maxChargePowerKwh;
    private BigDecimal minSohThreshold;
}
