package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.command.ConfirmArrivalCommand;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.service.business.IPaymentService;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.PaymentService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmArrivalUseCase implements IUseCase<ConfirmArrivalCommand, SwapTransactionResponse> {

    private final ISwapTransactionService swapTransactionService;
    private final IPaymentService paymentService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    @Transactional
    public SwapTransactionResponse execute(ConfirmArrivalCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User staff = swapTransactionService.getValidStaff(authenticatedUser.getUserId());
        SwapTransaction transaction = getValidTransactionForConfirmingArrival(request.getTransactionId());
        Station station = swapTransactionService.getValidStationFromStaffId(authenticatedUser.getUserId());
        if (!station.getId().equals(transaction.getStation().getId())) {
            throw new IllegalArgumentException("Transaction does not belong to this station.");
        }
        SwapTransaction confirmedArrivalTransaction = performConfirmingArrival(transaction);
        return ResponseMapper.mapToSwapTransactionResponse(confirmedArrivalTransaction);
    }

    private SwapTransaction getValidTransactionForConfirmingArrival(UUID transactionId) {
        SwapTransaction transaction = swapTransactionService.getTransactionById(transactionId);
        if (transaction.isTransactionNotConfirmedBy()) {
            throw new IllegalArgumentException("Transaction has not been confirmed yet. Cannot perform arrival confirmation.");
        }
        if (transaction.isTransactionExpired()) {
            throw new IllegalArgumentException("Transaction has expired.");
        }
        List<Payment> completedPayments = paymentService.findCompletedPaymentsWithTransaction(transaction);
        if (completedPayments.isEmpty()) {
            throw new IllegalArgumentException("No completed payments found for this transaction.");
        }
        if (transaction.getArrivalTime() != null) {
            throw new IllegalArgumentException("Transaction arrival time has already been set.");
        }
        if (transaction.isTransactionScheduled() && transaction.getScheduledTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Transaction has been scheduled and is not ready for arrival yet.");
        }
        return transaction;
    }

    private SwapTransaction performConfirmingArrival(SwapTransaction transaction) {
        transaction.setArrivalTime(LocalDateTime.now());
        return swapTransactionService.saveSwapTransaction(transaction);
    }

}
