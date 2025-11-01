package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetAllUnconfirmedSwapsUseCase implements IUseCase<UUID, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final StationStaffService stationStaffService;


    @Override
    public List<SwapTransaction> execute(UUID staffId) {
        StationStaff stationStaff = getValidStaff(staffId);
        return swapTransactionService.GetUnconfirmedSwapTransaction(stationStaff.getStation());
    }

    private StationStaff getValidStaff(UUID staffId) {
        return stationStaffService.getStationStaffById(staffId);
    }
}
