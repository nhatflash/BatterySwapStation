package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.ViewUnconfirmedSwapByAdminCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewUnconfirmedSwapByAdminUseCase implements IUseCase<ViewUnconfirmedSwapByAdminCommand, List<SwapTransaction>> {

    private final SwapTransactionService swapTransactionService;
    private final StationService stationService;
    private final UserService userService;

    @Override
    @Transactional
    public List<SwapTransaction> execute(ViewUnconfirmedSwapByAdminCommand viewUnconfirmedSwapByAdminCommand) {

        User admin = userService.getUserById(viewUnconfirmedSwapByAdminCommand.getAdminId());
        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("This user is not an admin.");
        }
        Station station = stationService.getByStationID(viewUnconfirmedSwapByAdminCommand.getStationId());
        return swapTransactionService.GetUnconfirmedSwapTransaction(station);
    }
}
