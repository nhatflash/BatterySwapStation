package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battery extends BaseEntity {

    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private BatteryModel model;

    @Column(nullable = false)
    private Integer capacityKwh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatteryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_station_id", nullable = false)
    public Station currentStation;

    @Column(precision = 5, scale = 2)
    private BigDecimal currentChargePercentage;

    private Integer totalChargeCycles;

    public LocalDateTime lastMaintenanceDate;

    private Integer totalSwapCount;

    private LocalDate manufactureDate;

    private LocalDate warrantyExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String notes;


}
