package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffDetailCommand {
    private UUID staffId;
    private EmploymentStatus status;
    private Money salary;
}
