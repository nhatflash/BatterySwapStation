package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryStateInterface {
    private UUID batteryId;
    private String serialNumber;
    private String batteryType;

    private Double stateOfHealth;
    private Integer chargeLevel;
    private Double temperature;
    private Double voltage;
    private Double current;
    private Double powerKwh;

    private UUID currentStationId;
    private LocalDateTime chargingStartedAt;
    private Integer estimatedMinutesToFull;

    private boolean abnormal;
    private String abnormalReason;

    private String alertLevel;
    private BatteryStatus status;
}
