package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public boolean isIdentityNumberExists(String identityNumber) {
        return userRepository.existsByIdentityNumber(identityNumber);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
