package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "battery_models", indexes = {
        @Index(name = "idx_type", columnList = "type", unique = true)
})
public class BatteryModel extends BaseEntity {

    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "type", nullable = false, length = 20))
    })
    private BatteryType type;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String chemistry;

    @Column(nullable = false)
    private Integer weightKg;

    private Integer warrantyMonths;

    private Integer maxChargePowerKwh;

    private BigDecimal minSohThreshold;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "battery_vehicles", joinColumns = @JoinColumn(name = "battery_model_id"))
    private List<String> compatibleVehicles;
}
