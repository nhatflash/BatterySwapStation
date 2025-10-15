package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.model.LogoutCommand;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.infrastructure.service.RedisSessionService;
import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase implements IUseCase<LogoutCommand, Void> {

    private final RedisSessionService redisSessionService;

    public LogoutUseCase(RedisSessionService redisSessionService) {
        this.redisSessionService = redisSessionService;
    }

    @Override
    public Void execute(LogoutCommand request) {
        redisSessionService.invalidateSession(request.getAccessToken());
        redisSessionService.invalidateRefreshToken(request.getUserId());
        return null;
    }
}
