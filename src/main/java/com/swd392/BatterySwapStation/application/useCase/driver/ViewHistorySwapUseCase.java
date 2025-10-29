package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.model.ViewHistorySwapCommand;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewHistorySwapUseCase implements IUseCase<ViewHistorySwapCommand, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final UserService userService;

    @Override
    @Transactional
    public List<SwapTransaction> execute(ViewHistorySwapCommand request) {
        User driver = userService.getUserById(request.getDriverId());
        return swapTransactionService.findByDriverAndStatus(driver, request.getStatus());
    }
}
