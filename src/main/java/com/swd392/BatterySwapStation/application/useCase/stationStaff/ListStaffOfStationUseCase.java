package com.swd392.BatterySwapStation.application.useCase.stationStaff;

import com.swd392.BatterySwapStation.application.service.business.IStationStaffService;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationStaffService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.application.model.response.StationStaffResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListStaffOfStationUseCase implements IUseCase<UUID, List<StationStaffResponse>> {

    private final IStationStaffService stationStaffService ;
    private final IUserService userService;

    @Override
    public List<StationStaffResponse> execute(UUID stationId) {
        var stationStaffs = stationStaffService.getStaffByStationId(stationId);
        if (stationStaffs == null || stationStaffs.isEmpty()) {
            throw new NotFoundException("No staff found for station with ID: " + stationId);
        }

        return stationStaffs.stream().map(staff -> {
            var user = userService.getUserById(staff.getStaff().getId());
            return StationStaffResponse.builder()
                    .staffId(staff.getStaff().getId())
                    .firstName(user.getFirstName())
                    .staffEmail(user.getEmail())
                    .lastName(user.getLastName())
                    .stationId(staff.getStation().getId())
                    .stationName(staff.getStation().getName())
                    .status(staff.getStatus())
                    .attachedAt(staff.getAttachedAt())
                    .salary(staff.getSalary().getAmount())
                    .build();
        }).toList();
    }
}