package com.swd392.BatterySwapStation.domain.entity;

import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime attachedAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "salary"))
    })
    private Money salary;
}
