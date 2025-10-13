package com.swd392.BatterySwapStation.domain.valueObject;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Money {
    private BigDecimal value;

    public Money() {}

    public Money(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }
}
