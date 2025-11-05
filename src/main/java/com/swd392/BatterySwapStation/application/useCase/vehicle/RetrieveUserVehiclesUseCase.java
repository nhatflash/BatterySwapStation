package com.swd392.BatterySwapStation.application.useCase.vehicle;

import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.VehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveUserVehiclesUseCase implements IUseCase<UUID, List<Vehicle>> {

    private final VehicleService vehicleService;
    private final UserService userService;

    @Override
    @Transactional
    public List<Vehicle> execute(UUID driverId) {
        User driver = userService.getUserById(driverId);
        return vehicleService.retrieveUserVehicles(driver);
    }
}
