package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.model.command.ViewBatteryInventoryCommand;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.application.service.business.IStationStaffService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViewBatteryInventoryUseCase implements IUseCase<ViewBatteryInventoryCommand, List<Battery>> {

    private final IStationStaffService stationStaffService;
    private final IBatteryService batteryService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public List<Battery> execute(ViewBatteryInventoryCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        var station = getStaffStation(authenticatedUser.getUserId());
        return batteryService.getByCurrentStationAndStatus(station, request.getBatteryStatus(), request.getPageIndex());
    }

    private Station getStaffStation(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        return stationStaff.getStation();
    }
}

