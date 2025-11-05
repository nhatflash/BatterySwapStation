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
public class AddNewBatteryCommand {
    private UUID currentStationId;
    private String serialNumber;
    private String type;
    private Integer capacityKwh;
    private String manufactureDate;
    private String warrantyExpiryDate;
    private String notes;
    private BigDecimal rentalPrice;
}
