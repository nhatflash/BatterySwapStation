package com.swd392.BatterySwapStation.application.useCase.profile;

import com.swd392.BatterySwapStation.application.service.business.IUserService;
import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrieveProfileDetailsUseCase implements IUseCase<String, User> {

    private final IUserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    public User execute(String userId) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        return userService.getUserById(authenticatedUser.getUserId());
    }
}
