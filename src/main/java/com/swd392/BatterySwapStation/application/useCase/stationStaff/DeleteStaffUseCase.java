package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.infrastructure.service.business.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteStaffUseCase implements IUseCase<UUID, Void> {

    private final StationStaffService stationStaffService;

    @Override
    public Void execute(UUID staffId) {
        var staff = stationStaffService.getStationStaffById(staffId);
        stationStaffService.deleteStationStaff(staff);
        return null;
    }
}
