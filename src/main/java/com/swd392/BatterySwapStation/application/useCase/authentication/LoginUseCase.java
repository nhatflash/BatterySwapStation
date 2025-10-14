package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.infrastructure.security.jwt.JwtUtil;
import com.swd392.BatterySwapStation.infrastructure.security.session.RedisSessionService;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.LoginRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginUseCase implements IUseCase<LoginRequest, LoginResponse> {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final RedisSessionService redisSessionService;

    private final UserService userService;

    public LoginUseCase(AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil,
                        RedisSessionService redisSessionService,
                        UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisSessionService = redisSessionService;
        this.userService = userService;
    }

    @Override
    public LoginResponse execute(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword())
        );
        User user = retrieveUserFromLogin(request);

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        storeUserSession(accessToken, refreshToken, user);

        return ResponseMapper.toLoginResponse(accessToken, refreshToken);
    }

    private User retrieveUserFromLogin(LoginRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if (user == null) throw new UsernameNotFoundException("User not found.");
        user.setLastLogin(LocalDateTime.now());
        return userService.saveUser(user);
    }

    private void storeUserSession(String accessToken, String refreshToken, User user) {
        redisSessionService.storeSession(accessToken, user.getId(), user.getRole().toString(), jwtUtil.getJwtAccessExpirationMs());
        redisSessionService.storeRefreshToken(refreshToken, user.getId(), jwtUtil.getJwtRefreshExpirationMs());
    }
}
