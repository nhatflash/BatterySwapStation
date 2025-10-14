package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stations", indexes = {
        @Index(name = "idx_name", columnList = "name",  unique = true),
        @Index(name = "idx_address", columnList = "address")
})
public class Station extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private Integer totalCapacity;

    @Column(nullable = false)
    private Integer totalSwapBays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StationStatus status;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Column(length = 20)
    private String contactPhone;

    private String contactEmail;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String imageUrl;
}
