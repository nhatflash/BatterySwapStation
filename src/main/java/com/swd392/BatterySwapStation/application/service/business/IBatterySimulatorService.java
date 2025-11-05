package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.model.BatteryState;

public interface IBatterySimulatorService {
    BatteryState simulateBatteryState(Battery battery);
}
