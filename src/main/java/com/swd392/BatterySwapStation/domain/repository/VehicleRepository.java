package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    boolean existsByVin(VIN vin);

    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findByDriver(User driver);

}
