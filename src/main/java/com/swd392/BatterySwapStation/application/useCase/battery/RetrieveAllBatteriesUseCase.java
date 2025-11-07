package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrieveAllBatteriesUseCase implements IUseCase<Integer, List<Battery>> {

    private final IBatteryService batteryService;

    @Override
    public List<Battery> execute(Integer pageIndex) {
        return batteryService.findAllBatteries(pageIndex);
    }
}
