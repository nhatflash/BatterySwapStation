package com.swd392.BatterySwapStation.application.common.mapper;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;

public class ResponseMapper {

    public static RegisterDriverResponse toRegisterDriverResponse(User user) {
        return RegisterDriverResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
