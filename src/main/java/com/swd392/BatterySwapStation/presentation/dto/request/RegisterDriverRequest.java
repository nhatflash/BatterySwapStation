package com.swd392.BatterySwapStation.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDriverRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must at least contain 8 characters.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    @Pattern(regexp = "^(0|\\+84|84)?(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Invalid vietnamese phone number.")
    private String phone;

    @NotBlank(message = "First name is required.")
    @Pattern(regexp = "^[A-Z][A-Za-z]*(?:\\s[A-Z][A-Za-z]*)*$", message = "Invalid first name.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Pattern(regexp = "^[A-Z][A-Za-z]*(?:\\s[A-Z][A-Za-z]*)*$", message = "Invalid last name.")
    private String lastName;

    @NotBlank(message = "Date of birth is required.")
    private String dateOfBirth;

    @Pattern(regexp = "^0[0-9]{11}$", message = "Invalid identity number.")
    private String identityNumber;
}
