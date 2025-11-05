package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.model.command.ViewHistorySwapCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewHistorySwapUseCase implements IUseCase<ViewHistorySwapCommand, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final UserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    @Transactional
    public List<SwapTransaction> execute(ViewHistorySwapCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User driver = userService.getUserById(authenticatedUser.getUserId());
        return swapTransactionService.findByDriverAndStatus(driver, request.getStatus());
    }
}
