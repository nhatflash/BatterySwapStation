package com.swd392.BatterySwapStation.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private UUID vehicleId;
    private String vin;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String batteryType;
    private UUID driverId;
}
