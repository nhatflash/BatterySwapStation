package com.swd392.BatterySwapStation.application.useCase.driver;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.RegisterDriverCommand;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterDriverRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterDriverUseCase implements IUseCase<RegisterDriverCommand, User> {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterDriverUseCase(UserService userService,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(RegisterDriverCommand request) {
        checkValidRequest(request);
        return createDriver(request);
    }

    private void checkValidRequest(RegisterDriverCommand request) {
        if (userService.isEmailExists(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (request.getPhone() != null && userService.isPhoneExists(request.getPhone())) {
            throw new IllegalArgumentException("Phone already exists!");
        }
        if (request.getIdentityNumber() != null && userService.isIdentityNumberExists(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Identity number already exists!");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
    }


    private User createDriver(RegisterDriverCommand request) {
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .identityNumber(request.getIdentityNumber())
                .dateOfBirth(DateStringMapper.getLocalDate(request.getDateOfBirth()))
                .status(UserStatus.ACTIVE)
                .role(UserRole.DRIVER)
                .build();
        return userService.saveUser(user);
    }
}
