package com.swd392.BatterySwapStation.application.service.internal;

import org.springframework.security.core.userdetails.UserDetails;

public interface ICustomUserDetailsService {
    UserDetails loadUserByUsername(String email);
}
