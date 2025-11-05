package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;

import java.util.List;
import java.util.UUID;

public interface IVehicleService {
    boolean existByVin(String vin);
    boolean existsByLicensePlate(String licensePlate);
    boolean isVehicleMatchesRequestBatteryType(Vehicle vehicle, BatteryType batteryType);
    Vehicle getVehicleById(UUID vehicleId);
    boolean isVehicleBelongToUser(User user, Vehicle vehicle);
    List<Vehicle> retrieveUserVehicles(User driver);
    Vehicle saveVehicle(Vehicle vehicle);
}
