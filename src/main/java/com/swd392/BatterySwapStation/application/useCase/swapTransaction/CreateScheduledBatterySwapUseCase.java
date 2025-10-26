package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.common.shared.PriceCalculator;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public SwapTransaction execute(CreateScheduledBatterySwapCommand request) {
        User requestDriver = getValidDriver(request.getDriverId());
        Vehicle requestedVehicle = getValidVehicle(request.getVehicleId(), requestDriver);
        BatteryType requestedBatteryType = requestedVehicle.getBatteryType();
        Station station = getValidStation(request.getStationId(),
                requestedBatteryType,
                requestedVehicle.getBatteryCapacity());
        SwapTransaction newScheduledTransaction = swapTransactionService.createScheduledTransaction(
                requestDriver,
                requestedVehicle,
                station,
                DateStringMapper.getLocalDateTime(request.getScheduledTime()),
                request.getNotes()
        );
        if (swapTransactionService.isVehicleFirstSwap(requestedVehicle)) {
            newScheduledTransaction.setSwapPrice(
                    new Money(BigDecimal.valueOf(PriceCalculator.FIRST_SWAP_PRICE * requestedVehicle.getBatteryCapacity())));
        }
        addOldBatteryTransactionIfExists(requestedVehicle, newScheduledTransaction);
        return swapTransactionService.saveSwapTransaction(newScheduledTransaction);
    }

    private void addOldBatteryTransactionIfExists(Vehicle vehicle, SwapTransaction swapTransaction) {
        List<Battery> oldVehicleBatteries = getOldBatteryInVehicle(vehicle);
        if (!oldVehicleBatteries.isEmpty()) {
            List<BatteryTransaction> batteryTransactions = swapTransaction.getBatteryTransactions();
            for (Battery oldBattery : oldVehicleBatteries) {
                batteryTransactions.add(
                        BatteryTransaction.builder()
                                .oldBattery(oldBattery)
                                .swapTransaction(swapTransaction)
                                .build()
                );
            }
        }
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

    private Station getValidStation(UUID stationId, BatteryType batteryType, int vehicleBatteryCapacity) {
        Station station = stationService.getByStationID(stationId);
        if (!stationService.isStationOperational(station)) {
            throw new IllegalArgumentException("This station is not operating.");
        }
        if (!isStationHasEnoughBatteryType(station, batteryType, vehicleBatteryCapacity)) {
            throw new IllegalArgumentException("This station does not have enough this battery type.");
        }
        return station;
    }

    private boolean isStationHasEnoughBatteryType(Station station, BatteryType batteryType, int vehicleBatteryCapacity) {
        int currentCapacity = batteryService.countByCurrentStationAndModel(station.getId(), batteryType.getValue());
        return currentCapacity >= vehicleBatteryCapacity;
    }

    private List<Battery> getOldBatteryInVehicle(Vehicle vehicle) {
        SwapTransaction latestCompletedTransaction = swapTransactionService.getLatestCompletedVehicleTransaction(vehicle);
        if (latestCompletedTransaction == null) {
            return new ArrayList<>();
        }
        var latestCompletedBatteryTransactions = latestCompletedTransaction.getBatteryTransactions();
        if (latestCompletedBatteryTransactions == null || latestCompletedBatteryTransactions.isEmpty()) {
            return new ArrayList<>();
        }
        List<Battery> oldBatteriesInVehicle = new ArrayList<>();
        for (var latestCompletedBatteryTransaction : latestCompletedBatteryTransactions) {
            if (latestCompletedBatteryTransaction.getOldBattery() == null) {
                continue;
            }
            oldBatteriesInVehicle.add(latestCompletedBatteryTransaction.getOldBattery());
        }
        return oldBatteriesInVehicle;
    }


}
