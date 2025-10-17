package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveAllModelsUseCase implements IUseCase<Integer, List<BatteryModel>> {

    private final BatteryService batteryService;

    public RetrieveAllModelsUseCase(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    public List<BatteryModel> execute(Integer page) {
        return batteryService.findAllModels(page);
    }
}
