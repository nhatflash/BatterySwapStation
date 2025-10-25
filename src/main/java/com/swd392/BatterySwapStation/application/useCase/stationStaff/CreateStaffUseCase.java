package com.swd392.BatterySwapStation.application.useCase.stationStaff;


import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.CreateStationStaffCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.service.StationStaffService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class CreateStaffUseCase implements IUseCase<CreateStationStaffCommand, StationStaff> {

    @Autowired
    StationStaffService stationStaffService;

    @Autowired
    StationService stationService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public StationStaff execute(CreateStationStaffCommand request) {

        var station = stationService.getStationByName(request.getStationName());
        if (station == null) {
            throw new IllegalArgumentException("Station not found: " + request.getStationName());
        }

        // --- Kiểm tra User tồn tại theo Email và Role ---
        User staffUser = userService.getUserByEmailAndRole(request.getStaffEmail(), UserRole.STAFF);

        if (staffUser == null) {
            log.info("Staff {} not found. Creating new user...", request.getStaffEmail());

            // --- Kiểm tra trùng lặp Email, Phone, Identity ---
            validateDuplicateUserInfo(request);

            // --- Tạo mới User ---
            staffUser = User.builder()
                    .email(request.getStaffEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .identityNumber(request.getIdentityNumber())
                    .dateOfBirth(DateStringMapper.getLocalDate(request.getDateOfBirth()))
                    .status(UserStatus.ACTIVE)
                    .role(UserRole.STAFF)
                    .build();

            staffUser = userService.saveUser(staffUser);
        }

        // --- Kiểm tra user có đang là staff ở station khác không ---
        var existingStaff = stationStaffService.getStationStaffByUserId(staffUser.getId());
        if (existingStaff != null) {
            throw new IllegalArgumentException("This user is already assigned as a staff to another station!");
        }

        // --- Tạo station staff ---
        StationStaff newStaff = StationStaff.builder()
                .staffId(staffUser.getId())
                .station(station)
                .status(request.getStatus())
                .salary(new Money(request.getSalary()))
                .build();

        return stationStaffService.saveStationStaff(newStaff);
    }


    private void validateDuplicateUserInfo(CreateStationStaffCommand request) {
        if (userService.isEmailExists(request.getStaffEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (request.getPhone() != null && userService.isPhoneExists(request.getPhone())) {
            throw new IllegalArgumentException("Phone already exists!");
        }
        if (request.getIdentityNumber() != null && userService.isIdentityNumberExists(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Identity number already exists!");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
    }
}