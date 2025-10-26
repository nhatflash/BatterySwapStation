package com.swd392.BatterySwapStation.application.useCase.profile;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.UpdateProfileCommand;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
            updateEmail(user, request.getEmail());
        }
        if (isRequestNotNullOrNotEmpty(request.getPhone())) {
            updatePhone(user, request.getPhone());
        }
        if (isRequestNotNullOrNotEmpty(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (isRequestNotNullOrNotEmpty(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (isRequestNotNullOrNotEmpty(request.getIdentityNumber())) {
            updateIdentityNumber(user, request.getIdentityNumber());
        }
        if (isRequestNotNullOrNotEmpty(request.getDateOfBirth())) {
            updateDateOfBirth(user, request.getDateOfBirth());
        }
        if (isRequestNotNullOrNotEmpty(request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
    }

    private boolean isRequestNotNullOrNotEmpty(String request) {
        return request != null && !request.isEmpty();
    }

    private void updateEmail(User user, String email) {
        if (!user.getEmail().equals(email) && userService.isEmailExists(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }
        user.setEmail(email);
    }

    private void updatePhone(User user, String phone) {
        if (user.getPhone() != null && !user.getPhone().equals(phone) && userService.isPhoneExists(phone)) {
            throw new IllegalArgumentException("Phone already exists!");
        }
        user.setPhone(phone);
    }

    private void updateIdentityNumber(User user, String identityNumber) {
        if (user.getIdentityNumber() != null && !user.getIdentityNumber().equals(identityNumber) && userService.isIdentityNumberExists(identityNumber)) {
            throw new IllegalArgumentException("Identity number already exists!");
        }
        user.setIdentityNumber(identityNumber);
    }

    private void updateDateOfBirth(User user, String dateOfBirthStr) {
        LocalDate dateOfBirth = DateStringMapper.getLocalDate(dateOfBirthStr);
        if (!userService.isEnoughAge(dateOfBirth)) {
            throw new IllegalArgumentException("You must be at least 18 years old to register as a driver!");
        }
        user.setDateOfBirth(dateOfBirth);
    }
}
