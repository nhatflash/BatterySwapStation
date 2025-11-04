package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.presentation.dto.response.StationStaffResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListAllStaffUseCase implements IUseCase<Void, List<StationStaffResponse>> {

    private final StationStaffService stationStaffService;
    private final UserService userService;

    @Override
    public List<StationStaffResponse> execute(Void request) {
        var staffs = stationStaffService.getAllStaff();

        return staffs.stream().map(staff -> {
            var user = userService.getUserById(staff.getStaff().getId());
            return StationStaffResponse.builder()
                    .staffId(staff.getStaff().getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .staffEmail(user.getEmail())
                    .stationId(staff.getStation().getId())
                    .stationName(staff.getStation().getName())
                    .status(staff.getStatus())
                    .attachedAt(staff.getAttachedAt())
                    .salary(staff.getSalary().getAmount())
                    .build();
        }).toList();
    }
}