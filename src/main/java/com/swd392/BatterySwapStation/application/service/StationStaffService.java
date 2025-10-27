package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.infrastructure.repository.StationStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StationStaffService {

    private final StationStaffRepository stationStaffRepository;
    private final UserService userService;


    // Lưu StationStaff
    public StationStaff saveStationStaff(StationStaff stationStaff) {
        return stationStaffRepository.save(stationStaff);
    }

    //  Xóa StationStaff (và đổi status của User sang DELETED nếu không ACTIVE)
    public void deleteStationStaff(StationStaff stationStaff) {
        var user = userService.getUserById(stationStaff.getStaff().getId());

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException("Cannot delete active user with ID: " + user.getId());
        }

        user.setStatus(UserStatus.DELETED);
        userService.saveUser(user);

        stationStaffRepository.delete(stationStaff);
    }


    //  Tìm nhân viên trạm theo UUID của User (staff_id)
    public StationStaff getStationStaffById(UUID userId) {
        return stationStaffRepository.findByStaff_Id(userId)
                .orElseThrow(() -> new NotFoundException("Station staff not found with User ID: " + userId));
    }

    //  Kiểm tra tồn tại theo UserId
    public boolean existsByStaffId(UUID userId) {
        return stationStaffRepository.existsByStaff_Id(userId);
    }

    //  Lấy danh sách nhân viên theo Station
    public List<StationStaff> getStaffByStationId(UUID stationId) {
        var staffList = stationStaffRepository.findByStation_Id(stationId);
        if (staffList.isEmpty()) {
            throw new NotFoundException("No staff found for Station ID: " + stationId);
        }
        return staffList;
    }


    // Lấy toàn bộ nhân viên
    public List<StationStaff> getAllStaff() {
        return stationStaffRepository.findAll();
    }

    // Lấy nhân viên theo UserId (nếu không cần exception)
    public StationStaff getStationStaffByUserId(UUID userId) {
        return stationStaffRepository.findByStaff_Id(userId).orElse(null);
    }
}