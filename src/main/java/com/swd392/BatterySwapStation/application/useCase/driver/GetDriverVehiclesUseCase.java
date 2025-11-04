package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.service.VehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetDriverVehiclesUseCase implements IUseCase<UUID, List<Vehicle>> {

    private final VehicleService vehicleService;
    private final UserService  userService;

    public GetDriverVehiclesUseCase(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public List<Vehicle> execute(UUID driverId) {
        User driver = userService.getUserById(driverId);

        return vehicleService.retrieveUserVehicles(driver);
    }
}
