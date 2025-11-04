package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.Gender;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_phone", columnList = "phone", unique = true),
        @Index(name = "idx_user_identity_number", columnList = "identityNumber", unique = true)
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, length = 20, unique = true)
    private String identityNumber;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status =  UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.DRIVER;

    private LocalDateTime lastLogin;

}
