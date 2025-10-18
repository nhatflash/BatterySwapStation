package com.swd392.BatterySwapStation.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String dateOfBirth;
    private String avatarUrl;
}
