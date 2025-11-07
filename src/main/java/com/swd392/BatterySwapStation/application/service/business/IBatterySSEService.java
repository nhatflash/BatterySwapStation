package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.model.BatteryState;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

public interface IBatterySSEService {
    SseEmitter createEmitter(UUID stationId);
    void broadcastBatteryUpdate(UUID stationId, BatteryState batteryState);
    void broadCastAlert(UUID stationId, String level, String message, UUID batteryId);
    int getTotalConnectionsCount();
    Map<String, Object> getConnectionStats();
}
