package com.swd392.BatterySwapStation.application.service.internal;

import com.swd392.BatterySwapStation.infrastructure.service.internal.CustomUserDetailsService;
import com.swd392.BatterySwapStation.infrastructure.service.internal.RedisSessionService;
import com.swd392.BatterySwapStation.infrastructure.service.internal.TokenService;

public interface IInternalServiceProviders {

    CustomUserDetailsService getCustomUserDetailsService();
    RedisSessionService getRedisSessionService();
    TokenService getTokenService();
}
