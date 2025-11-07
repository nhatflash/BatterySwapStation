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

@Service
@RequiredArgsConstructor
public class RetrieveAllBatteriesUseCase implements IUseCase<Integer, List<BatteryResponse>> {

    private final IBatteryService batteryService;

    @Override
    public List<BatteryResponse> execute(Integer pageIndex) {
        List<Battery> allBatteries = batteryService.findAllBatteries(pageIndex);
        return allBatteries.stream().map(ResponseMapper::mapToBatteryResponse).toList();
    }
}
