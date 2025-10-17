package com.swd392.BatterySwapStation.presentation.dto.request;

import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewBatteryRequest {

    @NotBlank(message = "Battery serial number is required.")
    private String serialNumber;

    @NotBlank(message = "Battery type is required.")
    private String type;

    @NotNull(message = "Battery capacity is required.")
    private Integer capacityKwh;

    @NotBlank(message = "Battery manufacture date is required.")
    private String manufactureDate;

    @NotBlank(message = "Battery warranty expiry date is required.")
    private String warrantyExpiryDate;

    private String notes;
}
