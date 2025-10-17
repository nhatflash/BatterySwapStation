package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.model.UpdateStaffDetailCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStaffStatusUseCase implements IUseCase<UpdateStaffDetailCommand, StationStaff> {

    private final StationStaffService stationStaffService;

    @Override
    public StationStaff execute(UpdateStaffDetailCommand request) {
        var staff = stationStaffService.getStationStaffById(request.getStaffId());

        staff.setStatus(request.getStatus() != null ? request.getStatus() : staff.getStatus());
        staff.setSalary(request.getSalary() != null ? new Money(request.getSalary().getAmount()) : staff.getSalary());


        return stationStaffService.saveStationStaff(staff);
    }
}