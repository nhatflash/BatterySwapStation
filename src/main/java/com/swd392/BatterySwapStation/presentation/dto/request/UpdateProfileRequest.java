package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @Email(message = "Invalid email.")
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String avatarUrl;
}
