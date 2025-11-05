package com.swd392.BatterySwapStation.application.useCase.feedback;

import com.swd392.BatterySwapStation.application.model.command.CreateFeedbackCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.valueObject.Rating;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateFeedbackUseCase implements IUseCase<CreateFeedbackCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final StationService stationService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    @Transactional
    public SwapTransaction execute(CreateFeedbackCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = swapTransactionService.getValidDriver(authenticatedUser.getUserId());
        SwapTransaction transaction = swapTransactionService.getTransactionById(request.getTransactionId());
        checkSwapTransactionReadyForFeedback(transaction, driver);
        return processSwapTransactionFeedback(transaction, request);
    }

    private void checkSwapTransactionReadyForFeedback(SwapTransaction transaction, User driver) {
        if (transaction.getSwapEndTime() == null) {
            throw new IllegalArgumentException("This swap transaction is not over yet.");
        }
        if (!transaction.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException("This swap transaction does not belong to this user.");
        }
    }

    private SwapTransaction processSwapTransactionFeedback(SwapTransaction transaction, CreateFeedbackCommand request) {
        transaction.setDriverFeedback(request.getFeedback());
        transaction.setDriverRating(request.getRating());
        SwapTransaction savedTransaction = swapTransactionService.saveSwapTransaction(transaction);
        updateStationAverageRating(savedTransaction);
        return savedTransaction;
    }

    private void updateStationAverageRating(SwapTransaction transaction) {
        Station station = stationService.getByStationID(transaction.getStation().getId());
        List<SwapTransaction> stationTransactions = swapTransactionService.findByStationAndSwapEndTimeIsNotNull(station);
        if (stationTransactions.isEmpty()) return;
        int numberOfEndedTransactions = 0;
        int totalRating = 0;
        for (SwapTransaction stationTransaction : stationTransactions) {
            if (stationTransaction.getDriverRating() == null) continue;
            totalRating += stationTransaction.getDriverRating();
            numberOfEndedTransactions++;
        }
        BigDecimal averageRating = BigDecimal.valueOf(totalRating / numberOfEndedTransactions);
        station.setAverageRating(new Rating(averageRating));
        stationService.saveStation(station);
    }
}
