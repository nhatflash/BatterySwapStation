package com.swd392.BatterySwapStation.presentation.dto.response;


import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID userId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private LocalDate dateOfBirth;
    private String avatarUrl;
    private UserStatus status;
    private UserRole role;
    private LocalDateTime lastLogin;
}
