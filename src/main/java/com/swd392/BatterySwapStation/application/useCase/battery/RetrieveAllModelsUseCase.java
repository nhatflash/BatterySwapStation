package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrieveAllModelsUseCase implements IUseCase<Integer, List<BatteryModelResponse>> {

    private final IBatteryService batteryService;

    @Override
    public List<BatteryModelResponse> execute(Integer page) {
        List<BatteryModel> allModels = batteryService.findAllModels(page);
        return allModels.stream().map(ResponseMapper::toBatteryModelResponse).toList();
    }
}
