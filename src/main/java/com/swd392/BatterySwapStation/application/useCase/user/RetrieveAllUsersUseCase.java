package com.swd392.BatterySwapStation.application.useCase.user;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.response.UserResponse;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveAllUsersUseCase implements IUseCase<Integer, List<UserResponse>> {

    private final IUserService userService;

    public RetrieveAllUsersUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserResponse> execute(Integer page) {
        List<User> allUsers = userService.getAllUsers(page);
        return allUsers.stream().map(ResponseMapper::mapToUserResponse).toList();
    }
}
