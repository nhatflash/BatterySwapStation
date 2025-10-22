package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateScheduledBatterySwapUseCase implements IUseCase<CreateScheduledBatterySwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final BatteryService batteryService;
    private final StationService stationService;
    private final UserService userService;
    private final VehicleService vehicleService;

    public CreateScheduledBatterySwapUseCase(SwapTransactionService swapTransactionService,
                                             BatteryService batteryService,
                                             StationService stationService,
                                             UserService userService,
                                             VehicleService vehicleService) {
        this.swapTransactionService = swapTransactionService;
        this.batteryService = batteryService;
        this.stationService = stationService;
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @Override
    public SwapTransaction execute(CreateScheduledBatterySwapCommand request) {
        var driver = getValidDriver(request.getDriverId());
        var vehicle = getValidVehicle(request.getVehicleId(), driver);
        var requestedBatteryType = vehicle.getBatteryType();
        int batteryCount = vehicle.getBatteryCapacity();
        var station = getValidStation(request.getStationId(), requestedBatteryType, batteryCount);
        return swapTransactionService.createScheduledTransaction(
                driver,
                vehicle,
                station,
                DateStringMapper.getLocalDateTime(request.getScheduledTime()),
                request.getNotes()
        );
    }


    private User getValidDriver(UUID driverId) {
        User driver = userService.getUserById(driverId);
        if (!userService.isCorrectRole(driver, UserRole.DRIVER)) {
            throw new IllegalArgumentException("Only driver can perform this operation.");
        }
        return driver;
    }

    private Vehicle getValidVehicle(UUID vehicleId, User driver) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (!vehicle.getDriver().equals(driver)) {
            throw new IllegalArgumentException("This vehicle is not assigned to this driver.");
        }
        return vehicle;
    }

    private Station getValidStation(UUID stationId, BatteryType batteryType, int batteryQuantity) {
        Station station = stationService.getByStationID(stationId);
        if (!stationService.isStationOperational(station)) {
            throw new IllegalArgumentException("This station is not operating.");
        }
        if (!isStationHasEnoughBatteryType(station, batteryType, batteryQuantity)) {
            throw new IllegalArgumentException("This station does not have enough this battery type.");
        }
        return station;
    }

    private boolean isStationHasEnoughBatteryType(Station station, BatteryType batteryType, int batteryQuantity) {
        int currentCapacity = batteryService.countByCurrentStationAndModel(station.getId(), batteryType.getValue());
        return currentCapacity >= batteryQuantity;
    }


}
