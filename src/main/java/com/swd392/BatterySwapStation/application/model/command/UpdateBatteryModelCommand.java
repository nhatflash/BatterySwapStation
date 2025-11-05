package com.swd392.BatterySwapStation.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBatteryModelCommand {
    private UUID modelId;
    private String type;
    private String manufacturer;
    private String chemistry;
    private Integer weightKg;
    private Integer warrantyMonths;
    private Integer maxChargePowerKwh;
    private BigDecimal minSohThreshold;
}
