package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.infrastructure.service.business.StationStaffService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetAllUnconfirmedSwapsUseCase implements IUseCase<String, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final StationStaffService stationStaffService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    public List<SwapTransaction> execute(String request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        StationStaff stationStaff = getValidStaff(authenticatedUser.getUserId());
        return swapTransactionService.GetUnconfirmedSwapTransaction(stationStaff.getStation());
    }

    private StationStaff getValidStaff(UUID staffId) {
        return stationStaffService.getStationStaffById(staffId);
    }
}
