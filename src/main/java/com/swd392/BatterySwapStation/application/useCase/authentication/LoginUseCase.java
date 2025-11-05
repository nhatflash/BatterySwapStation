package com.swd392.BatterySwapStation.application.useCase.authentication;

import com.swd392.BatterySwapStation.application.model.command.LoginCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.infrastructure.security.jwt.JwtUtil;
import com.swd392.BatterySwapStation.infrastructure.service.internal.RedisSessionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginUseCase implements IUseCase<LoginCommand, Map<String, String>> {

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
    public Map<String, String> execute(LoginCommand request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword())
        );
        User user = retrieveUserFromLogin(request);

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        storeUserSession(accessToken, refreshToken, user);

        return new HashMap<>() {
            {
                put("accessToken", accessToken);
                put("refreshToken", refreshToken);
            }
        };
    }

    private User retrieveUserFromLogin(LoginCommand request) {
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
