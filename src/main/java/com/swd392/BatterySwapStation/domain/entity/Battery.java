package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
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

    @Column(nullable = false)
    private Integer totalChargeCycles;

    public LocalDateTime lastMaintenanceDate;

    @Column(nullable = false)
    private Integer totalSwapCount;

    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate warrantyExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "rental_price"))
    })
    private Money rentalPrice;
}
