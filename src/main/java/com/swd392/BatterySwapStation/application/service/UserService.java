package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.infrastructure.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final int PAGE_SIZE = 10;

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

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers(int page) {
        if (page < 1) throw new IllegalArgumentException("Request page must be equal or greater than 1.");
        int pageIndex = page - 1;
        Pageable pageable = PageRequest.of(pageIndex, PAGE_SIZE);
        return userRepository.findAll(pageable).getContent();
    }

    public List<User> getUsersByRole(int page, UserRole role) {
        if (page < 1) throw new IllegalArgumentException("Request page must be equal or greater than 1.");
        int pageIndex = page - 1;
        Pageable pageable = PageRequest.of(pageIndex, PAGE_SIZE);
        return userRepository.findUsersByRole(pageable, role).getContent();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ============ Find User by Name and Role ===============
    public User getUserByEmailAndRole(String email, UserRole role) {
        return userRepository.findByEmailAndRole(email, role).orElse(null);
    }


    public boolean isCorrectRole(User user, UserRole role) {
        return user.getRole().equals(role);
    }

    public boolean isEnoughAge(LocalDate birthDate) {
        return birthDate.isBefore(LocalDate.now().minusYears(18));
    }
}
