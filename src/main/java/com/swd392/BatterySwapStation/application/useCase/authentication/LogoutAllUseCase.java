package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.infrastructure.service.RedisSessionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogoutAllUseCase implements IUseCase<UUID, Void> {

    private final RedisSessionService redisSessionService;

    public LogoutAllUseCase(RedisSessionService redisSessionService) {
        this.redisSessionService = redisSessionService;
    }

    @Override
    public Void execute(UUID userId) {
        redisSessionService.invalidAllUserSessions(userId);
        return null;
    }
}
