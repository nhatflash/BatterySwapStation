package com.swd392.BatterySwapStation.application.useCase.profile;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.UpdateProfileCommand;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileUseCase implements IUseCase<UpdateProfileCommand, User> {

    private final UserService userService;

    public UpdateProfileUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User execute(UpdateProfileCommand request) {
        User user = userService.getUserById(request.getUserId());
        updateProfile(request, user);
        return userService.saveUser(user);
    }

    private void updateProfile(UpdateProfileCommand request, User user) {
        if (isRequestNotNullOrNotEmpty(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (isRequestNotNullOrNotEmpty(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (isRequestNotNullOrNotEmpty(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (isRequestNotNullOrNotEmpty(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (isRequestNotNullOrNotEmpty(request.getIdentityNumber())) {
            user.setIdentityNumber(request.getIdentityNumber());
        }
        if (isRequestNotNullOrNotEmpty(request.getDateOfBirth())) {
            user.setDateOfBirth(DateStringMapper.getLocalDate(request.getDateOfBirth()));
        }
        if (isRequestNotNullOrNotEmpty(request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
    }

    private boolean isRequestNotNullOrNotEmpty(String request) {
        return request != null && !request.isEmpty();
    }
}
