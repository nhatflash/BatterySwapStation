package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.StationStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StationStaffService {

    private  final StationStaffRepository stationStaffRepository;


    public StationStaff saveStationStaff(StationStaff stationStaff) {
        return stationStaffRepository.save(stationStaff);
    }

    public Void deleteStationStaff(StationStaff stationStaff) {
        stationStaffRepository.delete(stationStaff);
        return null;
    }

    public StationStaff getStationStaffById(UUID staffId) {
        return stationStaffRepository.findByStaffId(staffId)
                .orElseThrow(() -> new NotFoundException("Station staff not found with ID: " + staffId));
    }

    public boolean existsByStaffId(UUID staffId) {
        return stationStaffRepository.existsByStaffId(staffId);
    }
    public List<StationStaff> getStaffByStationId(UUID stationId) {
        return stationStaffRepository.findByStation_Id(stationId);
    }

    public List<StationStaff> getAllStaff() {
        return stationStaffRepository.findAll();
    }
}
