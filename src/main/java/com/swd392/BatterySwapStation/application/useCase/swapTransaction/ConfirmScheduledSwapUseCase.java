package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.ConfirmScheduledSwapCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ConfirmScheduledSwapUseCase implements IUseCase<ConfirmScheduledSwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final UserService userService;
    private final StationStaffService stationStaffService;
    private final BatteryService batteryService;

    public ConfirmScheduledSwapUseCase(SwapTransactionService swapTransactionService,
                                       UserService userService,
                                       StationStaffService stationStaffService,
                                       BatteryService batteryService) {
        this.swapTransactionService = swapTransactionService;
        this.userService = userService;
        this.stationStaffService = stationStaffService;
        this.batteryService = batteryService;
    }

    @Override
    public SwapTransaction execute(ConfirmScheduledSwapCommand request) {
        var staff = getValidStaff(request.getStaffId());
        var transaction = getValidSwapTransaction(request.getTransactionId());
        var vehicle = transaction.getVehicle();
        var oldBatteries = getOldBatteryInVehicle(vehicle);
        var station = getValidStation(request.getStaffId());

        return confirmTransaction(transaction, staff, station, request.getBatteryIds(), oldBatteries);
    }

    private SwapTransaction getValidSwapTransaction(UUID transactionId) {
        var transaction = swapTransactionService.getTransactionById(transactionId);
        if (transaction.getConfirmedBy() != null || !transaction.getStatus().equals(TransactionStatus.SCHEDULED)) {
            throw new IllegalArgumentException("Transaction has already been confirmed or it is not scheduled.");
        }
        return transaction;
    }

    private User getValidStaff(UUID staffId) {
        var staff = userService.getUserById(staffId);
        if (staff.getStatus() != UserStatus.ACTIVE || !staff.getRole().equals(UserRole.STAFF)) {
            throw new IllegalArgumentException("This staff member is not active or is not a staff member.");
        }
        return staff;
    }

    private Station getValidStation(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        var station = stationStaff.getStation();
        if (station.getCurrentCapacity() == 0) {
            throw new IllegalArgumentException("Station has no battery.");
        }
        if (station.getStatus() != StationStatus.OPERATIONAL) {
            throw new IllegalArgumentException("Station is not operational.");
        }
        return station;
    }

    private Battery getValidBattery(UUID batteryId, UUID stationId) {
        var battery = batteryService.findByBatteryId(batteryId);
        if (battery.getCurrentStation() == null || battery.getCurrentStation().getId() != stationId) {
            throw new IllegalArgumentException("Battery is not assigned to this station.");
        }
        return battery;
    }

    private List<Battery> getOldBatteryInVehicle(Vehicle vehicle) {
        var oldTransaction = swapTransactionService.getLatestCompletedVehicleTransaction(vehicle);
        if (oldTransaction == null) return null;
        var oldBatteryTransactions = oldTransaction.getBatteryTransactions();
        List<Battery> oldBatteries = new ArrayList<>();
        for (var oldBatteryTransaction : oldBatteryTransactions) {
            if (oldBatteryTransaction.getOldBattery() == null) continue;
            oldBatteries.add(oldBatteryTransaction.getOldBattery());
        }
        return oldBatteries;
    }

    private List<UUID> getOldBatteryIdsInVehicle(Vehicle vehicle) {
        var oldBatteries = getOldBatteryInVehicle(vehicle);
        if (oldBatteries == null || oldBatteries.isEmpty()) return null;
        List<UUID> oldBatteryIds = new ArrayList<>();
        for (var battery : oldBatteries) {
            oldBatteryIds.add(battery.getId());
        }
        return oldBatteryIds;
    }

    private SwapTransaction confirmTransaction(SwapTransaction transaction, User staff, Station station, List<UUID> batteryIds, List<Battery> oldBatteries) {
        transaction.setConfirmedBy(staff);

        int vehicleBatteryCount = transaction.getVehicle().getBatteryCapacity();
        if (batteryIds.size() != vehicleBatteryCount || oldBatteries.size() != vehicleBatteryCount) {
            throw new IllegalArgumentException("Number of batteries in request does not match the vehicle's capacity.");
        }
        Set<BatteryTransaction> batteryTransactions = new HashSet<>();
        for (int i = 0; i < vehicleBatteryCount; i++) {
            var newBattery = getValidBattery(batteryIds.get(i), station.getId());
            var oldBattery = oldBatteries.get(i);
            var newBatteryTransaction = BatteryTransaction.builder()
                    .oldBattery(oldBattery)
                    .newBattery(newBattery)
                    .swapTransaction(transaction)
                    .build();
            batteryTransactions.add(newBatteryTransaction);
        }
        transaction.setBatteryTransactions(batteryTransactions);
        transaction.setStatus(TransactionStatus.CONFIRMED);
        return swapTransactionService.saveSwapTransaction(transaction);
    }
}
