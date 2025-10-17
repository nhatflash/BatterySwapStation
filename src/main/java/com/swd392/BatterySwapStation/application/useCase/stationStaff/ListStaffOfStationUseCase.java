package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListStaffOfStationUseCase implements IUseCase<UUID, List<StationStaff>> {

    private final StationStaffService stationStaffService ;

    @Override
    public List<StationStaff> execute(UUID stationId) {
        var stationStaffs = stationStaffService.getStaffByStationId(stationId);
        if (stationStaffs == null || stationStaffs.isEmpty()) {
            throw new NotFoundException("No staff found for station with ID: " + stationId);
        }
        return stationStaffs;
    }
}
