package com.swd392.BatterySwapStation.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDriverCommand {
    private String email;

    private String password;

    private String confirmPassword;

    private String phone;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String identityNumber;
}
