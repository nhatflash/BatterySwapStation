package com.swd392.BatterySwapStation.domain.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Embeddable
@EqualsAndHashCode
@Getter
public class Rating {

    @Column(name = "rate", precision = 2, scale = 1, nullable = false)
    private BigDecimal rate;

    public Rating() {}

    public Rating(BigDecimal rate) {
        checkRate(rate);
        this.rate = rate;
    }

    private void checkRate(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rate cannot be negative");
        }
        if (rate.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Rate cannot be greater than 5");
        }
    }
}
