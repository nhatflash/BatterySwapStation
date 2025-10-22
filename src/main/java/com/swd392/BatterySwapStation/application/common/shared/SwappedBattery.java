package com.swd392.BatterySwapStation.application.common.shared;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwappedBattery {
    @NotBlank(message = "Battery type is required.")
    private String batteryType;

    @NotBlank(message = "Quantity is required.")
    private Integer quantity;
}
