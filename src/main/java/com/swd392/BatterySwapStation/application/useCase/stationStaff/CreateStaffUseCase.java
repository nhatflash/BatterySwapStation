package com.swd392.BatterySwapStation.application.useCase.stationStaff;


import com.swd392.BatterySwapStation.application.model.CreateStationStaffCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class CreateStaffUseCase implements IUseCase<CreateStationStaffCommand, StationStaff> {

    @Autowired
    StationStaffService stationStaffService;

    @Autowired
    StationService stationService;

    @Autowired
    UserService userService;

    @Override
    public StationStaff execute(CreateStationStaffCommand request) {

        var station = stationService.getStationByName(request.getStationName());
        var staffUser = userService.getUserByEmailAndRole(request.getStaffEmail(), UserRole.STAFF);

        if (stationStaffService.existsByStaffId(staffUser.getId())) {
            throw new IllegalArgumentException("Staff member with email " + request.getStaffEmail() + " is already assigned to a station.");
        }

        StationStaff newStaff = StationStaff.builder()
                .staffId(staffUser.getId())
                .station(station)
                .status(request.getStatus())
                .salary(new Money(request.getSalary()))
                .build();

        return stationStaffService.saveStationStaff(newStaff);
    }
}
