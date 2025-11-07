package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.service.internal.ITokenService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements IUseCase<String, String> {

    private final ITokenService tokenService;

    @Override
    public String execute(String refreshToken) {
        return tokenService.refreshAccessToken(refreshToken);
    }
}
