package com.swd392.BatterySwapStation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateStationStaffRequest {
    @Schema(description = "Mail của nhân viên", example = "vit@gmail.com")
    @NotBlank(message = "Staff email is required")
    @Email(message = "Invalid email.")
    private String staffEmail;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must at least contain 8 characters.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    private String phone;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Date of birth is required.")
    private String dateOfBirth;

    private String identityNumber;

    private String AvatarUrl;

    @Schema(description = "Tên của trạm sạc", example = "Station 1")
    @NotBlank(message = "Station name is required")
    private String stationName;

    @Schema(description = "Mức lương nhân viên", example = "5000000")
    @NotNull(message = "Salary is required")
    private BigDecimal salary;
}
