package com.swd392.BatterySwapStation.domain.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
public class BatteryType {

    @Column(nullable = false, length = 20, name = "battery_type")
    private String value;

    protected BatteryType() {}

    public BatteryType(String value) {
        validateBatteryType(value);
        this.value = value;
    }

    private void validateBatteryType(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Battery type cannot be null or blank");
        }

        String upperValue = value.toUpperCase().trim();
        final Pattern BATTERY_TYPE_PATTERN = Pattern.compile("^[A-Z]{2,4}-\\d{2,3}$");
        if (!BATTERY_TYPE_PATTERN.matcher(upperValue).matches()) {
            throw new IllegalArgumentException("Invalid battery type");
        }

    }

}
