package com.swd392.BatterySwapStation.application.useCase.profile;

import com.swd392.BatterySwapStation.infrastructure.service.business.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.infrastructure.security.user.AuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.security.user.ICurrentAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrieveProfileDetailsUseCase implements IUseCase<String, User> {

    private final UserService userService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;

    @Override
    public User execute(String userId) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        return userService.getUserById(authenticatedUser.getUserId());
    }
}
