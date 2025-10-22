package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.UserRepository;
import com.swd392.BatterySwapStation.domain.repository.VehicleRepository;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import com.swd392.BatterySwapStation.presentation.dto.response.VehicleResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public boolean existByVin(String vin) {
        VIN vinObj = new VIN(vin);
        return vehicleRepository.existsByVin(vinObj);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleRepository.existsByLicensePlate(licensePlate);
    }

    public boolean isVehicleMatchesRequestBatteryType(Vehicle vehicle, BatteryType batteryType) {
        return vehicle.getBatteryType().equals(batteryType);
    }

    public Vehicle getVehicleById(UUID vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found."));
    }

    public boolean isVehicleBelongToUser(User user, Vehicle vehicle) {
        return vehicle.getDriver().equals(user);
    }

    public List<Vehicle> retrieveUserVehicles(User driver) {
        return vehicleRepository.findByDriver(driver);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
