package com.swd392.BatterySwapStation.infrastructure.security.session;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SessionData {
    private UUID userId;
    private String role;
    private Long expiresAt;
}
