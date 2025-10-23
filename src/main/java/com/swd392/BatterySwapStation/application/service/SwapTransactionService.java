package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class SwapTransactionService {

    private final SwapTransactionRepository swapTransactionRepository;

    private static final double TRANSACTION_FEE = 100000.0;

    public SwapTransactionService(SwapTransactionRepository swapTransactionRepository) {
        this.swapTransactionRepository = swapTransactionRepository;
    }

    public SwapTransaction createScheduledTransaction(User driver,
                                                      Vehicle vehicle,
                                                      Station station,
                                                      LocalDateTime scheduledTime,
                                                      String notes) {
        int vehicleBatteryCount = vehicle.getBatteryCapacity();
        return SwapTransaction.builder()
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
