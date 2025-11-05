package com.swd392.BatterySwapStation.infrastructure.service.internal;

import com.swd392.BatterySwapStation.application.service.internal.IInternalServiceProviders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalServiceProviders implements IInternalServiceProviders {
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisSessionService redisSessionService;
    private final TokenService tokenService;

    @Override
    public CustomUserDetailsService getCustomUserDetailsService() {
        return customUserDetailsService;
    }

    @Override
    public RedisSessionService getRedisSessionService() {
        return redisSessionService;
    }

    @Override
    public TokenService getTokenService() {
        return tokenService;
    }

}
