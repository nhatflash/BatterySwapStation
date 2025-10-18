package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveAllBatteriesUseCase implements IUseCase<Integer, List<Battery>> {

    private final BatteryService batteryService;

    public RetrieveAllBatteriesUseCase(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    public List<Battery> execute(Integer pageIndex) {
        return batteryService.findAllBatteries(pageIndex);
    }
}
