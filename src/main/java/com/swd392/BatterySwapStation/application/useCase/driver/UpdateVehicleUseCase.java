package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.model.command.UpdateVehicleCommand;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.application.service.business.IVehicleService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateVehicleUseCase implements IUseCase<UpdateVehicleCommand, Vehicle> {

    private final IVehicleService vehicleService;
    private final IUserService userService;
    private final ISwapTransactionService swapTransactionService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public Vehicle execute(UpdateVehicleCommand request) {
        Vehicle vehicle = vehicleService.getVehicleById(request.getVehicleId());
        checkValidFromRequest(request, vehicle);
        updateVehicle(request, vehicle);
        return vehicleService.saveVehicle(vehicle);
    }

    private void updateVehicle(UpdateVehicleCommand request, Vehicle vehicle) {
        if (isRequestStringNotNullOrEmpty(request.getVin())) {
            updateVin(vehicle, request.getVin());
        }
        if (isRequestStringNotNullOrEmpty(request.getMake())) {
            vehicle.setMake(request.getMake());
        }
        if (isRequestStringNotNullOrEmpty(request.getModel())) {
            vehicle.setModel(request.getModel());
        }
        if (request.getYear() != null) {
            vehicle.setYear(request.getYear());
        }
        if (isRequestStringNotNullOrEmpty(request.getLicensePlate())) {
            updateLicensePlate(vehicle, request.getLicensePlate());
        }
        if (isRequestStringNotNullOrEmpty(request.getBatteryType())) {
            vehicle.setBatteryType(new BatteryType(request.getBatteryType()));
        }
        if (request.getBatteryCapacity() != null) {
            vehicle.setBatteryCapacity(request.getBatteryCapacity());
        }
    }

    private void checkValidFromRequest(UpdateVehicleCommand request, Vehicle vehicle) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = userService.getUserById(authenticatedUser.getUserId());
        if (!vehicle.getDriver().equals(driver)) {
            throw new IllegalArgumentException("This vehicle is not assigned to this driver.");
        }
        List<SwapTransaction> auditTransactions = swapTransactionService.getAllSwapForVehicle(vehicle);
        if (!auditTransactions.isEmpty()) {
            throw new IllegalArgumentException("This vehicle has been used in swap transaction.");
        }
    }

    private boolean isRequestStringNotNullOrEmpty(String request) {
        return request != null && !request.isEmpty();
    }

    private void updateVin(Vehicle vehicle, String vin) {
        if (!vehicle.getVin().getValue().equals(vin) && vehicleService.existByVin(vin)) {
            throw new IllegalArgumentException("Vin already exists");
        }
        vehicle.setVin(new VIN(vin));
    }

    private void updateLicensePlate(Vehicle vehicle, String licensePlate) {
        if (!vehicle.getLicensePlate().equals(licensePlate) && vehicleService.existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("License plate already exists.");
        }
        vehicle.setLicensePlate(licensePlate);
    }
}
