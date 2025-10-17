package com.swd392.BatterySwapStation.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewBatteryCommand {
    private UUID currentStationId;
    private String serialNumber;
    private String type;
    private Integer capacityKwh;
    private String manufactureDate;
    private String warrantyExpiryDate;
    private String notes;
}
