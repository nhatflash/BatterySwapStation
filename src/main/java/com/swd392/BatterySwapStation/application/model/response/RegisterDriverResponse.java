package com.swd392.BatterySwapStation.application.model.response;

import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDriverResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private UserStatus status;
}
