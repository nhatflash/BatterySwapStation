package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.service.internal.RedisSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutAllUseCase implements IUseCase<String, Void> {

    private final RedisSessionService redisSessionService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    public Void execute(String request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        redisSessionService.invalidAllUserSessions(authenticatedUser.getUserId());
        return null;
    }
}
