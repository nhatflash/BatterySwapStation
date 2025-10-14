package com.swd392.BatterySwapStation.domain.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
public class Money {

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    public Money() {}

    public Money(BigDecimal value) {
        validate(value);
        this.amount = value.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        BigDecimal result = this.amount.add(other.amount);
        return new Money(result);
    }

    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);
        return new Money(result);
    }

    public Money multiply(BigDecimal multiplier) {;
        if (multiplier.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Multiplier cannot be negative!");
        }
        BigDecimal result = this.amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        return new Money(result);
    }

    public Money multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }

    public Money divide(double value) {
        BigDecimal divider = BigDecimal.valueOf(value);
        if (divider.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Divider cannot be negative!");
        }
        BigDecimal result = this.amount.divide(divider, RoundingMode.HALF_UP);
        return new Money(result);
    }

    public Money percentage(double value) {
        BigDecimal percent =  BigDecimal.valueOf(value);
        if (percent.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Percentage cannot be negative!");
        }
        return multiply(percent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }

    private void validate(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount cannot be negative.");
    }
}
