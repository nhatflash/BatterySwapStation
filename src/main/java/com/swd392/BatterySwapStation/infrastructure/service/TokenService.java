package com.swd392.BatterySwapStation.infrastructure.service;


import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.repository.UserRepository;
import com.swd392.BatterySwapStation.infrastructure.security.jwt.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RedisSessionService redisSessionService;
    private final UserRepository userRepository;

    public TokenService(JwtUtil jwtUtil,
                        RedisSessionService redisSessionService,
                        UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.redisSessionService = redisSessionService;
        this.userRepository = userRepository;
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        UUID userId = jwtUtil.getUserIdFromToken(refreshToken);
        String storedRefreshToken = redisSessionService.getRefreshToken(userId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token not found or invalid.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole().toString());

        redisSessionService.storeSession(newAccessToken, user.getId(), user.getRole().toString(), jwtUtil.getJwtAccessExpirationMs());
        return newAccessToken;
    }
}

