package com.swd392.BatterySwapStation.application.useCase.profile;

import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RetrieveProfileDetailsUseCase implements IUseCase<UUID, User> {

    private final UserService userService;

    public RetrieveProfileDetailsUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User execute(UUID userId) {
        return userService.getUserById(userId);
    }
}
