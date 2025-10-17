package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAllStaffUseCase implements IUseCase<Void, List<StationStaff>> {

    private final StationStaffService stationStaffService;

    @Override
    public List<StationStaff> execute(Void request) {
        return stationStaffService.getAllStaff();
    }
}