package com.swd392.BatterySwapStation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionData {
    private UUID userId;
    private String role;
    private Long expiresAt;
}
