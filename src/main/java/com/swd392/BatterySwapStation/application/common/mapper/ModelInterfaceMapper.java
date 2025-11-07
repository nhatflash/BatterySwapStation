package com.swd392.BatterySwapStation.application.common.mapper;

import com.swd392.BatterySwapStation.application.model.BatteryStateInterface;
import com.swd392.BatterySwapStation.domain.model.BatteryState;

public class ModelInterfaceMapper {

    public static BatteryStateInterface mapToBatteryStateInterface(BatteryState batteryState) {
        return BatteryStateInterface.builder()
                .batteryId(batteryState.getBatteryId())
                .serialNumber(batteryState.getSerialNumber())
                .batteryType(batteryState.getBatteryType())
                .stateOfHealth(batteryState.getStateOfHealth())
                .chargeLevel(batteryState.getChargeLevel())
                .temperature(batteryState.getTemperature())
                .voltage(batteryState.getVoltage())
                .current(batteryState.getCurrent())
                .powerKwh(batteryState.getPowerKwh())
                .currentStationId(batteryState.getCurrentStationId())
                .chargingStartedAt(batteryState.getChargingStartedAt())
                .estimatedMinutesToFull(batteryState.getEstimatedMinutesToFull())
                .abnormal(batteryState.isAbnormal())
                .abnormalReason(batteryState.getAbnormalReason())
                .alertLevel(batteryState.getAlertLevel())
                .status(batteryState.getStatus())
                .build();
    }
}
