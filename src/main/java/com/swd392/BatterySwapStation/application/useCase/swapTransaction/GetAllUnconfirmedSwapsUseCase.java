package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.service.business.IStationStaffService;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationStaffService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetAllUnconfirmedSwapsUseCase implements IUseCase<String, List<SwapTransactionResponse>> {

    private final ISwapTransactionService swapTransactionService;
    private final IStationStaffService stationStaffService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    public List<SwapTransactionResponse> execute(String request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        StationStaff stationStaff = getValidStaff(authenticatedUser.getUserId());
        List<SwapTransaction> unconfirmedTransactions = swapTransactionService.GetUnconfirmedSwapTransaction(stationStaff.getStation());
        return unconfirmedTransactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
    }

    private StationStaff getValidStaff(UUID staffId) {
        return stationStaffService.getStationStaffById(staffId);
    }
}
