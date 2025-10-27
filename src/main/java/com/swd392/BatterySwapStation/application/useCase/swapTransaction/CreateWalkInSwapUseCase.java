package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.CreateWalkInSwapCommand;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CreateWalkInSwapUseCase implements IUseCase<CreateWalkInSwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;

    public CreateWalkInSwapUseCase(SwapTransactionService swapTransactionService) {
        this.swapTransactionService = swapTransactionService;
    }

    @Override
    @Transactional
    public SwapTransaction execute(CreateWalkInSwapCommand request) {
        User staff = swapTransactionService.getValidStaff(request.getStaffId());
        User driver = swapTransactionService.getValidDriver(request.getDriverId());
        Vehicle vehicle = swapTransactionService.getValidVehicle(request.getVehicleId(), driver);
        Station station = swapTransactionService.getValidStation(request.getStaffId());
        List<Battery> vehicleOldBatteries = swapTransactionService.getOldBatteryInVehicle(vehicle);
        List<Battery> requestedNewBatteries = swapTransactionService.getRequestedNewBatteries(request.getBatteryIds(),
                station.getId(),
                vehicle.getBatteryType().getValue());
        return createWalkInTransaction(staff, vehicle, requestedNewBatteries, vehicleOldBatteries, driver, station);
    }

    private SwapTransaction createWalkInTransaction(User staff,
                                                    Vehicle vehicle,
                                                    List<Battery> requestedNewBatteries,
                                                    List<Battery> oldVehicleBatteries,
                                                    User driver,
                                                    Station station) {
        SwapTransaction walkInTransaction = swapTransactionService.createWalkInTransaction(driver, vehicle, station);
        walkInTransaction.setConfirmedBy(staff);
        int vehicleBatteryCapacity = vehicle.getBatteryCapacity();
        if (vehicleBatteryCapacity != requestedNewBatteries.size()) {
            throw new IllegalArgumentException("Number of batteries in request does not match the vehicle's capacity.");
        }
        List<BatteryTransaction> batteryTransactions = walkInTransaction.getBatteryTransactions();
        if (oldVehicleBatteries != null && !oldVehicleBatteries.isEmpty()) {
            if (oldVehicleBatteries.size() != requestedNewBatteries.size()) {
                throw new IllegalArgumentException("The old vehicle battery capacity does not match the requested new battery capacity.");
            }
            createWalkInBatteryTransactions(batteryTransactions, walkInTransaction, requestedNewBatteries, oldVehicleBatteries);
        } else {
            createInitialBatteryTransactions(batteryTransactions, walkInTransaction, requestedNewBatteries);
        }
        walkInTransaction.setBatteryTransactions(batteryTransactions);
        return swapTransactionService.saveSwapTransaction(walkInTransaction);
    }

    private void createInitialBatteryTransactions(List<BatteryTransaction> batteryTransactions,
                                                                      SwapTransaction transaction,
                                                                      List<Battery> requestedNewBatteries) {
        for (Battery requestedNewBattery : requestedNewBatteries) {
            batteryTransactions.add(
                    BatteryTransaction.builder()
                            .swapTransaction(transaction)
                            .newBattery(requestedNewBattery)
                            .build()
            );
        }
    }

    private void createWalkInBatteryTransactions(List<BatteryTransaction> batteryTransactions,
                                                                      SwapTransaction transaction,
                                                                      List<Battery> requestedNewBatteries,
                                                                      List<Battery> oldVehicleBatteries) {
        for (int i = 0; i < oldVehicleBatteries.size(); i++) {
            batteryTransactions.add(
                    BatteryTransaction.builder()
                            .swapTransaction(transaction)
                            .oldBattery(oldVehicleBatteries.get(i))
                            .newBattery(requestedNewBatteries.get(i))
                            .build()
            );
        }
    }

}
