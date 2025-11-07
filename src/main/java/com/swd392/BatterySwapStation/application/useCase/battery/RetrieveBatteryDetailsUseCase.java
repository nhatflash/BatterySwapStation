package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveBatteryDetailsUseCase implements IUseCase<UUID, Battery> {

    private final IBatteryService batteryService;


    @Override
    public Battery execute(UUID batteryId) {
        return batteryService.findByBatteryId(batteryId);
    }
}
