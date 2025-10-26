package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^(0|\\+84|84)?(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Invalid vietnamese phone number.")
    private String phone;

    @Pattern(regexp = "^[A-Z][A-Za-z]*(?:\\s[A-Z][A-Za-z]*)*$", message = "Invalid first name.")
    private String firstName;

    @Pattern(regexp = "^[A-Z][A-Za-z]*(?:\\s[A-Z][A-Za-z]*)*$", message = "Invalid last name.")
    private String lastName;

    @Pattern(regexp = "^0[0-9]{11}$", message = "Invalid identity number.")
    private String identityNumber;

    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String avatarUrl;
}
