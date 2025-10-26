package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.model.ViewBatteryInventoryCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ViewBatteryInventoryUseCase implements IUseCase<ViewBatteryInventoryCommand, List<Battery>> {

    private final StationStaffService stationStaffService;
    private final BatteryService batteryService;

    public ViewBatteryInventoryUseCase(StationStaffService stationStaffService, BatteryService batteryService) {
        this.stationStaffService = stationStaffService;
        this.batteryService = batteryService;
    }

    @Override
    @Transactional
    public List<Battery> execute(ViewBatteryInventoryCommand request) {
        var station = getStaffStation(request.getStaffId());
        return batteryService.getByCurrentStationAndStatus(station, request.getBatteryStatus(), request.getPageIndex());
    }

    private Station getStaffStation(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        return stationStaff.getStation();
    }
}

