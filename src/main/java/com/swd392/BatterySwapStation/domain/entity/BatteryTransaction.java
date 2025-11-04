package com.swd392.BatterySwapStation.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swap_transaction_id", nullable = false)
    private SwapTransaction swapTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_battery_id")
    private Battery oldBattery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_battery_id")
    private Battery newBattery;

}
