package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.application.service.internal.IInternalServiceProviders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements IUseCase<String, String> {

    private final IInternalServiceProviders internalServiceProviders;

    @Override
    public String execute(String refreshToken) {
        return internalServiceProviders.getTokenService().refreshAccessToken(refreshToken);
    }
}
