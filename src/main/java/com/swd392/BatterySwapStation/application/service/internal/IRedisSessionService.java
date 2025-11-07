package com.swd392.BatterySwapStation.application.service.internal;

import com.swd392.BatterySwapStation.domain.model.SessionData;

import java.util.UUID;

public interface IRedisSessionService {
    void storeSession(String sessionId, UUID userId, String role, Long expiration);
    SessionData getSession(String sessionId);
    void invalidateSession(String sessionId);
    void storeRefreshToken(String refreshToken, UUID userId, Long expiration);
    String getRefreshToken(UUID userId);
    void invalidateRefreshToken(UUID userId);
    void invalidAllUserSessions(UUID userId);

}
