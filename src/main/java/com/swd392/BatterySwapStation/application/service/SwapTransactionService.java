package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.BatteryTransactionRepository;
import com.swd392.BatterySwapStation.domain.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class SwapTransactionService {

    private final SwapTransactionRepository swapTransactionRepository;
    private final BatteryTransactionRepository batteryTransactionRepository;

    private static final double TRANSACTION_FEE = 100000.0;

    public SwapTransactionService(SwapTransactionRepository swapTransactionRepository,
                                  BatteryTransactionRepository batteryTransactionRepository) {
        this.swapTransactionRepository = swapTransactionRepository;
        this.batteryTransactionRepository = batteryTransactionRepository;
    }

    public SwapTransaction createScheduledTransaction(User driver,
                                                      Vehicle vehicle,
                                                      Station station,
                                                      LocalDateTime scheduledTime,
                                                      String notes) {
        int vehicleBatteryCount = vehicle.getBatteryCapacity();
        var newSwapTransaction = SwapTransaction.builder()
                .code(generateTransactionCode())
                .driver(driver)
                .vehicle(vehicle)
                .station(station)
                .scheduledTime(scheduledTime)
                .status(TransactionStatus.SCHEDULED)
                .type(SwapType.SCHEDULED)
                .swapPrice(new Money(BigDecimal.valueOf(vehicleBatteryCount * TRANSACTION_FEE)))
                .notes(notes)
                .build();
        return saveSwapTransaction(newSwapTransaction);
    }

    public SwapTransaction createWalkInTransaction(User driver,
                                                   Vehicle vehicle,
                                                   Station station) {
        int vehicleBatteryCount = vehicle.getBatteryCapacity();
        return SwapTransaction.builder()
                .code(generateTransactionCode())
                .driver(driver)
                .vehicle(vehicle)
                .station(station)
                .status(TransactionStatus.CONFIRMED)
                .swapPrice(new Money(BigDecimal.valueOf(vehicleBatteryCount * TRANSACTION_FEE)))
                .type(SwapType.WALK_IN)
                .build();
    }


    public SwapTransaction getTransactionById(UUID transactionId) {
        return swapTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
    }

    public SwapTransaction getLatestCompletedVehicleTransaction(Vehicle vehicle) {
        var swapTransactions =  swapTransactionRepository.findAllByVehicleOrderByIdDesc(vehicle, TransactionStatus.COMPLETED);
        if (swapTransactions.isEmpty()) return null;
        return swapTransactions.getFirst();
    }

    public Set<BatteryTransaction> getBatteryTransactionFromTransaction(SwapTransaction transaction) {
        return batteryTransactionRepository.findAllBySwapTransaction(transaction);
    }

    public List<SwapTransaction> GetUnconfirmedSwapTransaction(Station station) {
        return swapTransactionRepository.findAllByStation(station);
    }


    public SwapTransaction saveSwapTransaction(SwapTransaction swapTransaction) {
        return swapTransactionRepository.save(swapTransaction);
    }

    private String generateTransactionCode() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        int randomCode = new Random().nextInt(99999999);
        return String.format("TXN-%s-%08d", LocalDateTime.now().format(dtf),
                randomCode);
    }
}
