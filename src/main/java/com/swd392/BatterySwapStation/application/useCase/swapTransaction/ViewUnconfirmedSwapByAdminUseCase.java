package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.command.ViewUnconfirmedSwapByAdminCommand;
import com.swd392.BatterySwapStation.application.service.business.IStationService;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewUnconfirmedSwapByAdminUseCase implements IUseCase<ViewUnconfirmedSwapByAdminCommand, List<SwapTransaction>> {

    private final ISwapTransactionService swapTransactionService;
    private final IStationService stationService;
    private final IUserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    @Transactional
    public List<SwapTransaction> execute(ViewUnconfirmedSwapByAdminCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User admin = userService.getUserById(authenticatedUser.getUserId());
        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("This user is not an admin.");
        }
        Station station = stationService.getByStationID(request.getStationId());
        return swapTransactionService.GetUnconfirmedSwapTransaction(station);
    }
}
