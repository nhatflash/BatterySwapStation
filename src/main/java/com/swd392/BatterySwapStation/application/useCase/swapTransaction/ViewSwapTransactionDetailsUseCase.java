package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViewSwapTransactionDetailsUseCase implements IUseCase<UUID, SwapTransactionResponse> {

    private final ISwapTransactionService swapTransactionService;

    @Override
    public SwapTransactionResponse execute(UUID transactionId) {
        SwapTransaction retrievedTransaction = swapTransactionService.getTransactionById(transactionId);
        return ResponseMapper.mapToSwapTransactionResponse(retrievedTransaction);
    }
}
