package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatterySSEService;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.application.service.business.IBatterySimulatorService;
import com.swd392.BatterySwapStation.domain.model.BatteryState;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/battery-monitoring")
@SecurityRequirement(name = "bearerAuth")
public class BatteryMonitoringController {

    private final IBatterySSEService batterySSEService;
    private final IBatterySimulatorService batterySimulatorService;
    private final IBatteryService batteryService;

    @GetMapping(value = "/stream/{stationId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public SseEmitter streamBatteryUpdates(@PathVariable UUID stationId) {
        return batterySSEService.createEmitter(stationId);
    }


    @GetMapping("/station/{stationId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<BatteryState>>> getBatteryStates(@PathVariable UUID stationId) {
        var batteries = batteryService.findByCurrentStation(stationId);
        var response = batteries.stream()
                .map(batterySimulatorService::simulateBatteryState)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>("Battery states retrieved successfully", response));
    }


    @GetMapping("/battery/{batteryId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<BatteryState>> getBatteryState(@PathVariable UUID batteryId) {
        var battery = batteryService.findByBatteryId(batteryId);
        var response = batterySimulatorService.simulateBatteryState(battery);
        return ResponseEntity.ok(new ApiResponse<>("Battery state retrieved successfully", response));
    }


    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConnectionStats() {
        var response = batterySSEService.getConnectionStats();
        return ResponseEntity.ok(new ApiResponse<>("Connection stats: ", response));
    }
}
