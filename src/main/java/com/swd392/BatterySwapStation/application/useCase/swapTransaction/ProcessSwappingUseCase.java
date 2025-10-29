package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.ProcessSwappingCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessSwappingUseCase implements IUseCase<ProcessSwappingCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final StationService stationService;

    @Override
    @Transactional
    public SwapTransaction execute(ProcessSwappingCommand request) {
        User staff = swapTransactionService.getValidStaff(request.getStaffId());
        SwapTransaction swapTransaction;
        Station station;
        if (request.isProcessing()) {
            swapTransaction = getValidTransactionForBeginSwapping(request.getTransactionId());
            station = getValidStationForBeginSwapping(staff);
            return processStartSwappingTransaction(swapTransaction, station);
        }
        swapTransaction = getValidTransactionForEndSwapping(request.getTransactionId());
        station = swapTransactionService.getValidStationFromStaffId(staff.getId());
        return processEndSwappingTransaction(swapTransaction, station);
    }

    private SwapTransaction getValidTransactionForBeginSwapping(UUID transactionId) {
        SwapTransaction transaction = swapTransactionService.getTransactionById(transactionId);
        if (transaction.getArrivalTime() == null) {
            throw new IllegalArgumentException("This transaction has not been confirmed arrival yet.");
        }
        if (transaction.getSwapStartTime() != null) {
            throw new IllegalArgumentException("This transaction has begun swapping.");
        }
        return transaction;
    }

    private SwapTransaction getValidTransactionForEndSwapping(UUID transactionId) {
        SwapTransaction transaction = swapTransactionService.getTransactionById(transactionId);
        if (transaction.getSwapStartTime() == null) {
            throw new IllegalArgumentException("This transaction has not been start swapping.");
        }
        if (transaction.getSwapEndTime() != null) {
            throw new IllegalArgumentException("This transaction has ended swapping.");
        }
        if (!transaction.isTransactionInProgress()) {
            throw new IllegalArgumentException("This transaction is not in progress.");
        }
        return transaction;
    }

    private Station getValidStationForBeginSwapping(User staff) {
        Station station = swapTransactionService.getValidStationFromStaffId(staff.getId());
        if (station.getIdleSwapBays() <= 0) {
            throw new IllegalArgumentException("This station does not have any free swap bay yet. Please wait and try again, or end a swapping process to release a swap bay.");
        }
        return station;
    }

    private SwapTransaction processStartSwappingTransaction(SwapTransaction transaction, Station station) {
        transaction.setSwapStartTime(LocalDateTime.now());

        if (station.getIdleSwapBays() > 0) {
            station.setIdleSwapBays(station.getIdleSwapBays() - 1);
        }
        stationService.saveStation(station);
        transaction.setStatus(TransactionStatus.IN_PROGRESS);
        return swapTransactionService.saveSwapTransaction(transaction);
    }

    private SwapTransaction processEndSwappingTransaction(SwapTransaction transaction, Station station) {
        transaction.setSwapEndTime(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        station.setIdleSwapBays(station.getIdleSwapBays() + 1);
        stationService.saveStation(station);
        return swapTransactionService.saveSwapTransaction(transaction);
    }
}
