package com.swd392.BatterySwapStation.application.model.command;


import com.swd392.BatterySwapStation.domain.enums.DashBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashBoardCommand {
    private DashBoard type; // DAY, MONTH, YEAR
    private LocalDateTime targetDate; // ví dụ: 2025-12-01T00:00:00
}
