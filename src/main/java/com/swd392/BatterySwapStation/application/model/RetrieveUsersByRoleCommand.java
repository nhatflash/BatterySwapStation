package com.swd392.BatterySwapStation.application.model;

import com.swd392.BatterySwapStation.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveUsersByRoleCommand {
    private int page;
    private UserRole role;
}
