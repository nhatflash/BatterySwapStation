package com.swd392.BatterySwapStation.application.useCase.user;

import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.enums.UserRoleReq;
import com.swd392.BatterySwapStation.application.model.command.RetrieveUsersByRoleCommand;
import com.swd392.BatterySwapStation.application.model.response.UserResponse;
import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrieveUsersByRoleUseCase implements IUseCase<RetrieveUsersByRoleCommand, List<UserResponse>> {

    private final IUserService userService;

    public RetrieveUsersByRoleUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserResponse> execute(RetrieveUsersByRoleCommand request) {
        List<User> roleUsers = getRequestRoleUsers(request.getPage(), request.getRole());
        return roleUsers.stream().map(ResponseMapper::mapToUserResponse).toList();
    }

    private List<User> getRequestRoleUsers(int page, UserRoleReq role) {
        return switch (role) {
            case ADMIN -> userService.getUsersByRole(page, UserRole.ADMIN);
            case STAFF -> userService.getUsersByRole(page, UserRole.STAFF);
            case DRIVER -> userService.getUsersByRole(page, UserRole.DRIVER);
        };
    }
}
