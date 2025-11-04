package com.swd392.BatterySwapStation.infrastructure.security.user;

import com.swd392.BatterySwapStation.domain.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private UUID userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public static CustomUserDetails create(User user) {
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        return CustomUserDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .enabled(true)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
