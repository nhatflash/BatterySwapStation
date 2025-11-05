package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IUserService {
    boolean isEmailExists(String email);
    boolean isPhoneExists(String phone);
    boolean isIdentityNumberExists(String identityNumber);
    User getUserByEmail(String email);
    User getUserById(UUID id);
    List<User> getAllUsers(int page);
    List<User> getUsersByRole(int page, UserRole role);
    User saveUser(User user);
    User getUserByEmailAndRole(String email, UserRole role);
    boolean isCorrectRole(User user, UserRole role);
    boolean isEnoughAge(LocalDate birthDate);
}
