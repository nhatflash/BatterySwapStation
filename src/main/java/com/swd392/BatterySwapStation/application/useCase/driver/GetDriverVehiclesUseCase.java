package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.VehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDriverVehiclesUseCase implements IUseCase<String, List<Vehicle>> {

    private final VehicleService vehicleService;
    private final UserService  userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public List<Vehicle> execute(String driverId) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = userService.getUserById(authenticatedUser.getUserId());
        return vehicleService.retrieveUserVehicles(driver);
    }
}
