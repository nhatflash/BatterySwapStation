package com.swd392.BatterySwapStation.application.useCase.user;

import com.swd392.BatterySwapStation.application.model.command.RetrieveUsersByRoleCommand;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveUsersByRoleUseCase implements IUseCase<RetrieveUsersByRoleCommand, List<User>> {

    private final IUserService userService;

    public RetrieveUsersByRoleUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> execute(RetrieveUsersByRoleCommand request) {
        return userService.getUsersByRole(request.getPage(), request.getRole());
    }
}
