package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.ConfirmScheduledSwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ConfirmScheduledSwapUseCase implements IUseCase<ConfirmScheduledSwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final UserService userService;
    private final StationStaffService stationStaffService;
    private final BatteryService batteryService;
    private final PaymentService paymentService;

    public ConfirmScheduledSwapUseCase(SwapTransactionService swapTransactionService,
                                       UserService userService,
                                       StationStaffService stationStaffService,
                                       BatteryService batteryService,
                                       PaymentService paymentService) {
        this.swapTransactionService = swapTransactionService;
        this.userService = userService;
        this.stationStaffService = stationStaffService;
        this.batteryService = batteryService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public SwapTransaction execute(ConfirmScheduledSwapCommand request) {
        User staff = getValidStaff(request.getStaffId());
        SwapTransaction transaction = getValidSwapTransaction(request.getTransactionId());
        Vehicle vehicle = transaction.getVehicle();
        var station = getValidStation(request.getStaffId());
        List<Battery> requestedNewBatteries = getRequestedNewBatteries(request.getBatteryIds(),
                station.getId(),
                vehicle.getBatteryType().getValue());
        return confirmTransaction(transaction, staff, requestedNewBatteries);
    }

    private SwapTransaction getValidSwapTransaction(UUID transactionId) {
        var transaction = swapTransactionService.getTransactionById(transactionId);
        checkValidTransactionForSwapping(transaction);
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
        checkValidStation(station);
        return station;
    }

    private List<Battery> getRequestedNewBatteries(List<UUID> newBatteryIds, UUID stationId, String vehicleBatteryType) {
        if (newBatteryIds == null || newBatteryIds.isEmpty()) {
            throw new IllegalArgumentException("New batteries for swapping needs to be specified.");
        }
        List<Battery> newBatteries = new ArrayList<>();
        for (var newBatteryId : newBatteryIds) {
            newBatteries.add(getValidNewBatteryForSwapping(newBatteryId, stationId, vehicleBatteryType));
        }
        return newBatteries;
    }

    private Battery getValidNewBatteryForSwapping(UUID batteryId, UUID stationId, String vehicleBatteryType) {
        Battery battery = batteryService.findByBatteryId(batteryId);
        checkValidRequestBatterySwapping(battery, stationId, vehicleBatteryType);
        return battery;
    }

    private SwapTransaction confirmTransaction(SwapTransaction transaction,
                                               User staff,
                                               List<Battery> requestedNewBatteries) {
        transaction.setConfirmedBy(staff);
        int vehicleBatteryCapacity = transaction.getVehicle().getBatteryCapacity();
        if (!isRequestBatteryCountMatchVehicleBatteryCapacity(vehicleBatteryCapacity, requestedNewBatteries.size())) {
            throw new IllegalArgumentException("Number of batteries in request does not match the vehicle's capacity.");
        }
        List<BatteryTransaction> batteryTransactions = transaction.getBatteryTransactions();
        for (int i = 0; i < requestedNewBatteries.size(); i++) {
            batteryTransactions.get(i).setNewBattery(requestedNewBatteries.get(i));
        }
        transaction.setStatus(TransactionStatus.CONFIRMED);
        return swapTransactionService.saveSwapTransaction(transaction);
    }


    private void checkValidStation(Station station) {
        if (station.isCurrentCapacityEmpty()) {
            throw new IllegalArgumentException("Station currently has no battery.");
        }
        if (!station.isOperational()) {
            throw new IllegalArgumentException("This station is not operational.");
        }
        if (!station.isOnWorkingTime()) {
            throw new IllegalArgumentException("This station is not on working time.");
        }
    }


    private void checkValidRequestBatterySwapping(Battery battery, UUID stationId, String vehicleBatteryType) {
        if (!battery.isOnStation(stationId)) {
            throw new IllegalArgumentException("Battery is not assigned to this station: " + battery.getId());
        }
        if (!battery.isBatteryFull()) {
            throw new IllegalArgumentException("Battery is not full: " + battery.getId());
        }
        if (!battery.isMatchType(vehicleBatteryType)) {
            throw new IllegalArgumentException("Battery is not match the type for the request vehicle: " + battery.getId());
        }
    }

    private void checkValidTransactionForSwapping(SwapTransaction transaction) {
        if (!transaction.isTransactionNotConfirmedBy() || !transaction.isTransactionScheduled()) {
            throw new IllegalArgumentException("Transaction has already been confirmed or it is not scheduled.");
        }
        if (transaction.isTransactionExpired()) {
            throw new IllegalArgumentException("Transaction has expired.");
        }
        List<Payment> payments = paymentService.findAllWithOrderDescByTransactionId(transaction);
        if (payments != null && !payments.isEmpty()) {
            Payment latestPayment = payments.getFirst();
            if (latestPayment.isPaymentCompleted()) {
                throw new IllegalArgumentException("Payment has already been completed.");
            }
        }
    }

    private boolean isRequestBatteryCountMatchVehicleBatteryCapacity(int vehicleBatteryCapacity, int requestedBatteryCount) {
        return vehicleBatteryCapacity == requestedBatteryCount;
    }
}
