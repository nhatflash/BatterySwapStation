package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.model.command.LogoutCommand;
import com.swd392.BatterySwapStation.application.service.internal.IRedisSessionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.service.internal.RedisSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUseCase implements IUseCase<LogoutCommand, Void> {

    private final IRedisSessionService redisSessionService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    public Void execute(LogoutCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        redisSessionService.invalidateSession(request.getAccessToken());
        redisSessionService.invalidateRefreshToken(authenticatedUser.getUserId());
        return null;
    }
}
