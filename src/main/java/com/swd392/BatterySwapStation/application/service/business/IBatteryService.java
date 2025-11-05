package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;

import java.util.List;
import java.util.UUID;

public interface IBatteryService {
    boolean existsByBatteryType(String batteryType);
    BatteryModel findByBatteryType(String batteryType);
    Battery findByBatteryId(UUID batteryId);
    List<Battery> findAllBatteries(int page);
    BatteryModel findByModelId(UUID modelId);
    List<Battery> findByCurrentStation(UUID stationId);
    int countByCurrentStationAndModel(UUID stationId, String batteryType);
    List<BatteryModel> findAllModels(int page);
    BatteryModel saveBatteryModel(BatteryModel batteryModel);
    List<Battery> getByCurrentStationAndStatus(Station currentStation, BatteryStatus status, int page);
    Battery saveBattery(Battery battery);
}
