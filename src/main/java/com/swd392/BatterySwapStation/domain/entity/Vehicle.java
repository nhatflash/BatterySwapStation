package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.VIN;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_vin", columnList = "vin", unique = true),
        @Index(name = "idx_vehicle_license_plate", columnList = "licensePlate", unique = true)
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity{

    @Embedded
    private VIN vin;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    private Integer year;

    @Column(nullable = false)
    private String licensePlate;

    @Embedded
    private BatteryType batteryType;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;
}
