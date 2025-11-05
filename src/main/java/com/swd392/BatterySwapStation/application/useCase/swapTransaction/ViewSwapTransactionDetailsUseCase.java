package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViewSwapTransactionDetailsUseCase implements IUseCase<UUID, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;

    @Override
    public SwapTransaction execute(UUID transactionId) {
        return swapTransactionService.getTransactionById(transactionId);
    }
}
