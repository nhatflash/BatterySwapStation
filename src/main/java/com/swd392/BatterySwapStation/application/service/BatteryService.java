package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.repository.BatteryModelRepository;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.stereotype.Service;

@Service
public class BatteryService {

    private final BatteryModelRepository batteryModelRepository;

    public BatteryService(BatteryModelRepository batteryModelRepository) {
        this.batteryModelRepository = batteryModelRepository;
    }

    public boolean existsByBatteryType(String batteryType) {
        BatteryType type = new BatteryType(batteryType);
        return batteryModelRepository.existsByType(type);
    }

    public BatteryModel saveBatteryModel(BatteryModel batteryModel) {
        return batteryModelRepository.save(batteryModel);
    }
}
