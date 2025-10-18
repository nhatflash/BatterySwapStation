package com.swd392.BatterySwapStation.domain.valueObject;

import com.swd392.BatterySwapStation.domain.enums.HealthStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Embeddable
@Getter
@EqualsAndHashCode
public class SoH {

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    public SoH() {}

    public SoH(BigDecimal percentage) {
        checkSoH(percentage);
        this.percentage = percentage;
    }

    private void checkSoH(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) < 0 ||
                percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("State of health must be between 0 and 100");
        }
    }

    public boolean isHealthy() {
        return this.percentage.compareTo(BigDecimal.valueOf(80)) >= 0;
    }

    public boolean requiresMaintenance() {
        return this.percentage.compareTo(BigDecimal.valueOf(70)) < 0;
    }

    public boolean needToBeRetired() {
        return this.percentage.compareTo(BigDecimal.valueOf(50)) < 0;
    }

    public SoH degrate(BigDecimal degradationRate) {
        BigDecimal newPercentage = this.percentage.subtract(degradationRate);
        return new SoH(newPercentage.max(BigDecimal.ZERO));
    }

    public HealthStatus getStatus() {
        if (this.percentage.compareTo(BigDecimal.valueOf(90)) >= 0) {
            return HealthStatus.EXCELLENT;
        } else if (this.percentage.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return HealthStatus.GOOD;
        } else if (this.percentage.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return HealthStatus.FAIR;
        } else if (this.percentage.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return HealthStatus.POOR;
        } else if (this.percentage.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return HealthStatus.VERY_POOR;
        } else {
            return HealthStatus.CRITICAL;
        }
    }
}
