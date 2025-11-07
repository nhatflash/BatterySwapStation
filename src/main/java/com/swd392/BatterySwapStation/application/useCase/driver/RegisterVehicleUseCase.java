package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.command.RegisterVehicleCommand;
import com.swd392.BatterySwapStation.application.model.response.VehicleResponse;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.application.service.business.IVehicleService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.VehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RegisterVehicleUseCase implements IUseCase<RegisterVehicleCommand, VehicleResponse> {

    private final IVehicleService vehicleService;
    private final IUserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public VehicleResponse execute(RegisterVehicleCommand request) {
        checkValidFromRequest(request);
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = userService.getUserById(authenticatedUser.getUserId());
        Vehicle registeredVehicle = registerVehicle(request, driver);
        return ResponseMapper.toVehicleResponse(registeredVehicle);
    }

    private void checkValidFromRequest(RegisterVehicleCommand request) {
        if (vehicleService.existByVin(request.getVin())) {
            throw new IllegalArgumentException("Vin already exists");
        }
        if (vehicleService.existsByLicensePlate(request.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }
    }

    private Vehicle registerVehicle(RegisterVehicleCommand request, User driver) {
        Vehicle newVehicle = Vehicle.builder()
                .vin(new VIN(request.getVin()))
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(new BatteryType(request.getBatteryType()))
                .batteryCapacity(request.getBatteryCapacity())
                .isActive(true)
                .driver(driver)
                .build();
        return vehicleService.saveVehicle(newVehicle);
    }




}
