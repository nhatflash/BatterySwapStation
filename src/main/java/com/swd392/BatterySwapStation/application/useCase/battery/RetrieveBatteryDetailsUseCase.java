package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RetrieveBatteryDetailsUseCase implements IUseCase<UUID, Battery> {

    private final BatteryService batteryService;

    public RetrieveBatteryDetailsUseCase(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    public Battery execute(UUID batteryId) {
        return batteryService.findByBatteryId(batteryId);
    }
}
