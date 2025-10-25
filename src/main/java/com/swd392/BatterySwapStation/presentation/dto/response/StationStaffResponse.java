package com.swd392.BatterySwapStation.presentation.dto.response;

import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StationStaffResponse {
    private UUID staffId;
    private String staffEmail;
    private String firstName;
    private String lastName;
    private UUID stationId;
    private String stationName;
    private EmploymentStatus status;
    private LocalDateTime attachedAt;
    private BigDecimal salary;
}
