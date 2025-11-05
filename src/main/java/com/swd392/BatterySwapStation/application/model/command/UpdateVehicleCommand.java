package com.swd392.BatterySwapStation.application.model.command;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleCommand {
    private UUID vehicleId;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private String batteryType;
    private Integer batteryCapacity;
}
