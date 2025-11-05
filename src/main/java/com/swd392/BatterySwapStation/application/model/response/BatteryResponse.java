package com.swd392.BatterySwapStation.application.model.response;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryResponse {
    private UUID batteryId;
    private String serialNumber;
    private String type;
    private int capacityKwh;
    private BatteryStatus status;
    private String currentStationName;
    private BigDecimal currentChargePercentage;
    private int totalChargeCycles;
    private LocalDateTime lastMaintenanceDate;
    private int totalSwapCount;
    private LocalDate manufactureDate;
    private LocalDate warrantyExpiryDate;
    private String notes;
    private BigDecimal rentalPrice;
}
