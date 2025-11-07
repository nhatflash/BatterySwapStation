package com.swd392.BatterySwapStation.infrastructure.security.user;

import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentAuthenticatedUser implements ICurrentAuthenticatedUser {


    @Override
    public AuthenticatedUser getCurrentAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new IllegalStateException("Current user has not been authenticated yet.");
        }
        return new AuthenticatedUser(principal.getUserId(), principal.getEmail());
    }
}
