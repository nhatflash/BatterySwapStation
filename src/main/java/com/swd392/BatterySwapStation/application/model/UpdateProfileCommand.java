package com.swd392.BatterySwapStation.application.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileCommand {
    private UUID userId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String dateOfBirth;
    private String avatarUrl;
}
