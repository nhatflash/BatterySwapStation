package com.swd392.BatterySwapStation.application.security;

import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;

public interface ICurrentAuthenticatedUser {

    AuthenticatedUser getCurrentAuthenticatedUser();
}
