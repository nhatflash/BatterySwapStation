package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllUnconfirmedSwapsUseCase implements IUseCase<UUID, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final StationStaffService stationStaffService;

    public GetAllUnconfirmedSwapsUseCase(SwapTransactionService swapTransactionService,
                                         StationStaffService stationStaffService) {
        this.swapTransactionService = swapTransactionService;
        this.stationStaffService = stationStaffService;
    }

    @Override
    public List<SwapTransaction> execute(UUID staffId) {
        var stationStaff = getValidStaff(staffId);
        return swapTransactionService.GetUnconfirmedSwapTransaction(stationStaff.getStation());
    }

    private StationStaff getValidStaff(UUID staffId) {
        return stationStaffService.getStationStaffById(staffId);
    }
}
