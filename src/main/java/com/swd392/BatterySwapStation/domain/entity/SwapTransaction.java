package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "swap_transactions", indexes = {
        @Index(name = "idx_txn_code", columnList = "code", unique = true)
})
public class SwapTransaction extends BaseEntity {

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "swapTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BatteryTransaction> batteryTransactions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_staff_id")
    private User confirmedBy;

    private LocalDateTime scheduledTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime swapStartTime;
    private LocalDateTime swapEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwapType type;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "swap_price", precision = 19, scale = 2, nullable = false))
    })
    private Money swapPrice;

    @Column(length = 1000)
    private String notes;

    private Integer driverRating;

    @Column(length = 1000)
    private String driverFeedback;

}
