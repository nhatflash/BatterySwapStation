package com.swd392.BatterySwapStation.domain.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class VIN {

    @Column(nullable = false, unique = true, name = "vin", length = 17)
    private String value;

    protected VIN() {}

    public VIN(String value) {
        isValidVin(value);
        this.value = value;
    }

    public String getManufacturer() {
        return value.substring(0, 3);
    }

    public String getDescriptiveModel() {
        return value.substring(3, 9);
    }

    public String getIdentifier() {
        return value.substring(9);
    }

    private void isValidVin(String value) {
        final int VIN_LENGTH = 17;
        final String VIN_REGEX = "^[A-HJ-NPR-Z0-9]{17}$";
        if (value == null || value.length() != VIN_LENGTH || !value.matches(VIN_REGEX)) {
            throw new IllegalArgumentException("Invalid VIN");
        }
    }


}
