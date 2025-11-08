package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.enums.BatteryStatusReq;
import com.swd392.BatterySwapStation.application.model.command.ViewBatteryInventoryCommand;
import com.swd392.BatterySwapStation.application.model.response.BatteryResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.application.service.business.IStationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViewBatteryInventoryUseCase implements IUseCase<ViewBatteryInventoryCommand, List<BatteryResponse>> {

    private final IStationStaffService stationStaffService;
    private final IBatteryService batteryService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public List<BatteryResponse> execute(ViewBatteryInventoryCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        var station = getStaffStation(authenticatedUser.getUserId());
        List<Battery> stationBatteries =  getStationBatteriesWithRequestStatus(station, request.getBatteryStatus(), request.getPageIndex());
        return stationBatteries.stream().map(ResponseMapper::mapToBatteryResponse).toList();
    }

    private Station getStaffStation(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        return stationStaff.getStation();
    }

    private List<Battery> getStationBatteriesWithRequestStatus(Station station, BatteryStatusReq request, int pageIndex) {
        return switch (request) {
            case FULL -> batteryService.getByCurrentStationAndStatus(station, BatteryStatus.FULL, pageIndex);
            case IN_USE -> batteryService.getByCurrentStationAndStatus(station, BatteryStatus.IN_USE, pageIndex);
            case MAINTENANCE ->
                    batteryService.getByCurrentStationAndStatus(station, BatteryStatus.MAINTENANCE, pageIndex);
            case FAULTY -> batteryService.getByCurrentStationAndStatus(station, BatteryStatus.FAULTY, pageIndex);
            case RETIRED -> batteryService.getByCurrentStationAndStatus(station, BatteryStatus.RETIRED, pageIndex);
            case CHARGING -> batteryService.getByCurrentStationAndStatus(station, BatteryStatus.CHARGING, pageIndex);
            default -> throw new IllegalArgumentException("Invalid battery status request");
        };
    }
}

