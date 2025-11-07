package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.enums.TransactionStatusReq;
import com.swd392.BatterySwapStation.application.model.command.ViewHistorySwapCommand;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewHistorySwapUseCase implements IUseCase<ViewHistorySwapCommand, List<SwapTransactionResponse>> {

    private final ISwapTransactionService swapTransactionService;
    private final IUserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    @Transactional
    public List<SwapTransactionResponse> execute(ViewHistorySwapCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = userService.getUserById(authenticatedUser.getUserId());
        List<SwapTransaction> historyTransactions = getRequestHistoryTransactions(driver, request.getStatus());
        return historyTransactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
    }

    private List<SwapTransaction> getRequestHistoryTransactions(User driver, TransactionStatusReq status) {
        return switch (status) {
            case IN_PROGRESS -> swapTransactionService.findByDriverAndStatus(driver, TransactionStatus.IN_PROGRESS);
            case COMPLETED -> swapTransactionService.findByDriverAndStatus(driver, TransactionStatus.COMPLETED);
            case CANCELED -> swapTransactionService.findByDriverAndStatus(driver, TransactionStatus.CANCELED);
            case SCHEDULED -> swapTransactionService.findByDriverAndStatus(driver, TransactionStatus.SCHEDULED);
            case CONFIRMED -> swapTransactionService.findByDriverAndStatus(driver, TransactionStatus.CONFIRMED);
        };
    }
}
