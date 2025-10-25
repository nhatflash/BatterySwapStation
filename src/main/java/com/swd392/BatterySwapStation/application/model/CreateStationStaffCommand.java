package com.swd392.BatterySwapStation.application.model;


import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreateStationStaffCommand {
    private UUID stationId;
    private UUID staffId;
    private String stationName;
    private String StaffEmail;
    private EmploymentStatus status;
    private BigDecimal salary;
    private String identityNumber;
    private String phone;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String avatarUrl;
    private String password;
    private String confirmPassword;
}
