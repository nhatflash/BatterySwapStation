package com.swd392.BatterySwapStation.infrastructure.security.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SSEConnectionInfo {
    private SseEmitter emitter;
    private UUID userId;
    private LocalDateTime connectedAt;
}
