package com.swd392.BatterySwapStation.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd392.BatterySwapStation.domain.model.BatteryState;
import com.swd392.BatterySwapStation.infrastructure.security.sse.SSEConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class BatterySSEService {

    private static final Logger logger = LoggerFactory.getLogger(BatterySSEService.class);
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L;

    private final ObjectMapper objectMapper;

    public BatterySSEService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final Map<UUID, CopyOnWriteArrayList<SSEConnectionInfo>> connectionsByStation = new ConcurrentHashMap<>();


    public SseEmitter createEmitter(UUID stationId, UUID userId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        SSEConnectionInfo connectionInfo = new SSEConnectionInfo(emitter, userId, LocalDateTime.now());

        connectionsByStation.computeIfAbsent(stationId, k -> new CopyOnWriteArrayList<>()).add(connectionInfo);
        Runnable cleanup = () -> removeConnection(stationId, connectionInfo);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> {
            logger.error("SSE connection timed out for user {} on station {}", userId, stationId, e);
            cleanup.run();
        });

        logger.info("SSE connection created for user {} on station {} (Total: {})", userId, stationId, getConnectionsCount(stationId));

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(Map.of(
                            "message", "Connected to battery monitoring",
                            "stationId", stationId,
                            "timestamp", System.currentTimeMillis()
                    )));
        } catch (IOException e) {
            logger.error("SSE connection failed for user {} on station {}", userId, stationId, e);
        }
        return emitter;
    }


    @Transactional
    public void broadcastBatteryUpdate(UUID stationId, BatteryState batteryState) {
        CopyOnWriteArrayList<SSEConnectionInfo> connections = connectionsByStation.get(stationId);

        if (checkIfStationCollectionIsNull(connections, stationId)) {
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (SSEConnectionInfo connection : connections) {
            try {
                connection.getEmitter().send(SseEmitter.event()
                        .name("battery-update")
                        .data(batteryState)
                        .id(String.valueOf(System.currentTimeMillis())));
                successCount++;
            } catch (IOException e) {
                logger.error("Failed to send battery update to user {} on station {}", connection.getUserId(), stationId, e);
                removeConnection(stationId, connection);
                failCount++;
            }
        }
        logger.debug("Broadcast battery {} update: {} success, {} failed", batteryState.getBatteryId(), successCount, failCount);
    }


    @Transactional
    public void broadCastAlert(UUID stationId, String level, String message, UUID batteryId) {
        CopyOnWriteArrayList<SSEConnectionInfo> connections = connectionsByStation.get(stationId);
        if (checkIfStationCollectionIsNull(connections, stationId)) {
            return;
        }
        Map<String, Object> alert = Map.of(
                "type", "alert",
                "level", level,
                "message", message,
                "batteryId", batteryId,
                "timestamp", System.currentTimeMillis()
        );
        for (SSEConnectionInfo connection : connections) {
            try {
                connection.getEmitter().send(SseEmitter.event()
                        .name("alert")
                        .data(alert));
            } catch (IOException e) {
                logger.error("Failed to send alert to user {} on station {}", connection.getUserId(), stationId, e);
                removeConnection(stationId, connection);
            }
        }
        logger.info("Alert broadcasted to station {}: {} - {}", stationId, level, message);
    }

    private void removeConnection(UUID stationId, SSEConnectionInfo connectionInfo) {
        CopyOnWriteArrayList<SSEConnectionInfo> connections = connectionsByStation.get(stationId);
        if (connections != null) {
            connections.remove(connectionInfo);
            logger.info("SSE connection removed for user {} on station {} (Remaining: {})",
                    connectionInfo.getUserId(), stationId, connections.size());
            if (connections.isEmpty()) {
                connectionsByStation.remove(stationId);
            }
        }
    }

    private boolean checkIfStationCollectionIsNull(CopyOnWriteArrayList<SSEConnectionInfo> connections, UUID stationId) {
        if (connections == null || connections.isEmpty()) {
            logger.info("No connected clients for station {}", stationId);
            return true;
        }
        return false;
    }

    @Transactional
    public int getConnectionsCount(UUID stationId) {
        if (connectionsByStation.get(stationId) == null) {
            return 0;
        }
        return connectionsByStation.get(stationId).size();
    }


    @Transactional
    public int getTotalConnectionsCount() {
        return connectionsByStation.values().stream()
                .mapToInt(CopyOnWriteArrayList::size)
                .sum();
    }


    @Transactional
    public Map<String, Object> getConnectionStats() {
        return Map.of(
                "totalConnections", getTotalConnectionsCount(),
                "activeStations", connectionsByStation.size(),
                "connectionsByStation", connectionsByStation.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()))
        );
    }



}
