package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateStationStaffRequest {
    @Schema(description = "Mail của nhân viên", example = "Nguyen Van A")
    @NotBlank(message = "Staff email is required")
    private String staffEmail;

    @Schema(description = "Tên của trạm sạc", example = "Station 1")
    @NotBlank(message = "Station name is required")
    private String stationName;

    @Schema(description = "Mức lương nhân viên", example = "5000000")
    @NotNull(message = "Salary is required")
    private BigDecimal salary;
}
