package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.repository.VehicleRepository;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import org.springframework.stereotype.Service;

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

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
