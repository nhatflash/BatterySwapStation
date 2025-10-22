package com.swd392.BatterySwapStation.application.useCase.user;

import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveAllUsersUseCase implements IUseCase<Integer, List<User>> {

    private final UserService userService;

    public RetrieveAllUsersUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> execute(Integer page) {
        return userService.getAllUsers(page);
    }
}
