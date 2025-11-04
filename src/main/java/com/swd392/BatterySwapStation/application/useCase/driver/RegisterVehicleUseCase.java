package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.model.RegisterVehicleCommand;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.service.VehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterVehicleUseCase implements IUseCase<RegisterVehicleCommand, Vehicle> {

    private final VehicleService vehicleService;
    private final UserService userService;

    public RegisterVehicleUseCase(VehicleService vehicleService,
                                  UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Vehicle execute(RegisterVehicleCommand request) {
        checkValidFromRequest(request);
        User driver = userService.getUserById(request.getUserId());
        return registerVehicle(request, driver);
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
