package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
public class CreateScheduledBatterySwapUseCase implements IUseCase<CreateScheduledBatterySwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final BatteryService batteryService;
    private final UserService userService;

    public CreateScheduledBatterySwapUseCase(SwapTransactionService swapTransactionService,
                                             BatteryService batteryService,
                                             UserService userService) {
        this.swapTransactionService = swapTransactionService;
        this.batteryService = batteryService;
        this.userService = userService;
    }

    @Override
    public SwapTransaction execute(CreateScheduledBatterySwapCommand request) {
        User driver = userService.getUserById(request.getDriverId());
        if (!userService.isCorrectRole(driver, UserRole.DRIVER)) {
            throw new IllegalArgumentException("Only driver can perform this operation.");
        }

        return null;
    }
}
