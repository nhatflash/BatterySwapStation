package com.swd392.BatterySwapStation.presentation.mapper;

import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.presentation.dto.response.*;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.VehicleResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

    public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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

    public static CreateStationResponse mapToCreateResponse(Station station) {
        return CreateStationResponse.builder()
                .stationId(station.getId().toString())
                .name(station.getName())
                .address(station.getAddress())
                .totalCapacity(station.getTotalCapacity())
                .totalSwapBays(station.getTotalSwapBays())
                .status(station.getStatus())
                .openingTime(station.getOpeningTime().toString())
                .closingTime(station.getClosingTime().toString())
                .contactPhone(station.getContactPhone())
                .contactEmail(station.getContactEmail())
                .description(station.getDescription())
                .imageUrl(station.getImageUrl())
                .build();
    }
    public static UpdateStationResponse mapToUpdateStationResponse(Station station) {
        return UpdateStationResponse.builder()
                .stationId(station.getId().toString())
                .name(station.getName())
                .address(station.getAddress())
                .totalCapacity(station.getTotalCapacity())
                .totalSwapBays(station.getTotalSwapBays())
                .status(station.getStatus())
                .openingTime(station.getOpeningTime().toString())
                .closingTime(station.getClosingTime().toString())
                .contactPhone(station.getContactPhone())
                .contactEmail(station.getContactEmail())
                .description(station.getDescription())
                .imageUrl(station.getImageUrl())
                .build();
    }

    public static ChangeStationStatusResponse mapToChangeStationStatusResponse(Station station) {
        return ChangeStationStatusResponse.builder()
                .stationId(station.getId())
                .newStatus(station.getStatus())
                .build();
    }

    public  static StationStaffResponse mapToStationStaffResponse(StationStaff staff, UserService userService) {

        var user = userService.getUserById(staff.getStaffId());
        return StationStaffResponse.builder()
                .staffId(staff.getStaffId())
                .staffEmail(user.getEmail())
                .stationId(staff.getStation().getId())
                .stationName(staff.getStation().getName())
                .salary(staff.getSalary().getAmount())
                .status(staff.getStatus())
                .attachedAt(staff.getAttachedAt())
                .build();
    }
}
