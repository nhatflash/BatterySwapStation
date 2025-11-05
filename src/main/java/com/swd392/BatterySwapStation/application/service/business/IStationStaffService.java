package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.StationStaff;

import java.util.List;
import java.util.UUID;

public interface IStationStaffService {
    StationStaff saveStationStaff(StationStaff stationStaff);
    void deleteStationStaff(StationStaff stationStaff);
    StationStaff getStationStaffById(UUID userId);
    boolean existsByStaffId(UUID userId);
    List<StationStaff> getStaffByStationId(UUID stationId);
    List<StationStaff> getAllStaff();
    StationStaff getStationStaffByUserId(UUID userId);
}
