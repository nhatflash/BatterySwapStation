package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.response.BatteryResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveBatteryDetailsUseCase implements IUseCase<UUID, BatteryResponse> {

    private final IBatteryService batteryService;


    @Override
    public BatteryResponse execute(UUID batteryId) {

        Battery retrievedBattery = batteryService.findByBatteryId(batteryId);
        return ResponseMapper.mapToBatteryResponse(retrievedBattery);
    }
}
