package com.swd392.BatterySwapStation.presentation.mapper;

import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.VehicleResponse;

public class ResponseMapper {

    public static RegisterDriverResponse toRegisterDriverResponse(User user) {
        return RegisterDriverResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public static LoginResponse toLoginResponse(String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .vehicleId(vehicle.getId())
                .vin(vehicle.getVin().getValue())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .batteryType(vehicle.getBatteryType().getValue())
                .driverId(vehicle.getDriver().getId())
                .build();
    }

    public static BatteryModelResponse toBatteryModelResponse(BatteryModel model) {
        return BatteryModelResponse.builder()
                .modelId(model.getId())
                .type(model.getType().getValue())
                .manufacturer(model.getManufacturer())
                .chemistry(model.getChemistry())
                .weightKg(model.getWeightKg())
                .warrantyMonths(model.getWarrantyMonths())
                .maxChargePowerKwh(model.getMaxChargePowerKwh())
                .minSohThreshold(model.getMinSohThreshold())
                .compatibleVehicles(model.getCompatibleVehicles())
                .build();
    }
}
