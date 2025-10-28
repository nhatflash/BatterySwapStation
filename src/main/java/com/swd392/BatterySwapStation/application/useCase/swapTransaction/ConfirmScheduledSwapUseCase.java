package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.ConfirmScheduledSwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ConfirmScheduledSwapUseCase implements IUseCase<ConfirmScheduledSwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final PaymentService paymentService;

    public ConfirmScheduledSwapUseCase(SwapTransactionService swapTransactionService,
                                       PaymentService paymentService) {
        this.swapTransactionService = swapTransactionService;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public SwapTransaction execute(ConfirmScheduledSwapCommand request) {
        User staff = swapTransactionService.getValidStaff(request.getStaffId());
        SwapTransaction transaction = getValidSwapTransactionForConfirmation(request.getTransactionId());
        Vehicle vehicle = transaction.getVehicle();
        var station = swapTransactionService.getValidStationFromStaffId(request.getStaffId());
        List<Battery> requestedNewBatteries = swapTransactionService.getRequestedNewBatteries(request.getBatteryIds(),
                station.getId(),
                vehicle.getBatteryType().getValue());
        return confirmTransaction(transaction, staff, requestedNewBatteries);
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
            if (batteryTransactions.isEmpty()) {
                batteryTransactions.add(BatteryTransaction.builder()
                        .newBattery(requestedNewBatteries.get(i))
                        .build());
            } else {
                batteryTransactions.get(i).setNewBattery(requestedNewBatteries.get(i));
            }
        }
        transaction.setStatus(TransactionStatus.CONFIRMED);
        return swapTransactionService.saveSwapTransaction(transaction);
    }



    private boolean isRequestBatteryCountMatchVehicleBatteryCapacity(int vehicleBatteryCapacity, int requestedBatteryCount) {
        return vehicleBatteryCapacity == requestedBatteryCount;
    }

    private SwapTransaction getValidSwapTransactionForConfirmation(UUID transactionId) {
        var transaction = swapTransactionService.getTransactionById(transactionId);
        checkValidTransactionForConfirmingSwapping(transaction);
        return transaction;
    }

    private void checkValidTransactionForConfirmingSwapping(SwapTransaction transaction) {
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
}
