package com.swd392.BatterySwapStation.presentation.mapper;

import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.presentation.dto.response.*;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.VehicleResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
                .currentCapacity(station.getCurrentCapacity())
                .idleSwapBays(station.getIdleSwapBays())
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

    public  static StationStaffResponse mapToStationStaffResponse(StationStaff staff) {

        return StationStaffResponse.builder()
                .staffId(staff.getStaffId())
                .stationId(staff.getStation().getId())
                .stationName(staff.getStation().getName())
                .salary(staff.getSalary().getAmount())
                .status(staff.getStatus())
                .attachedAt(staff.getAttachedAt())
                .build();
    }

    public static BatteryResponse mapToBatteryResponse(Battery battery) {
        return BatteryResponse.builder()
                .batteryId(battery.getId())
                .serialNumber(battery.getSerialNumber())
                .type(battery.getModel().getType().getValue())
                .capacityKwh(battery.getCapacityKwh())
                .status(battery.getStatus())
                .currentStationName(battery.getCurrentStation().getName())
                .currentChargePercentage(battery.getCurrentChargePercentage())
                .totalChargeCycles(battery.getTotalChargeCycles())
                .lastMaintenanceDate(battery.getLastMaintenanceDate())
                .totalSwapCount(battery.getTotalSwapCount())
                .manufactureDate(battery.getManufactureDate())
                .warrantyExpiryDate(battery.getWarrantyExpiryDate())
                .notes(battery.getNotes())
                .rentalPrice(battery.getRentalPrice().getAmount())
                .build();
    }

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identityNumber(user.getIdentityNumber())
                .dateOfBirth(user.getDateOfBirth())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .status(user.getStatus())
                .lastLogin(user.getLastLogin())
                .build();
    }

    public static SwapTransactionResponse mapToSwapTransactionResponse(SwapTransaction transaction) {
        List<BatteryTransactionResponse> responses = new ArrayList<>();
        for (var btr : transaction.getBatteryTransactions()) {
            responses.add(new BatteryTransactionResponse(btr.getOldBattery().getId(), btr.getNewBattery().getId()));
        }
        return SwapTransactionResponse.builder()
                .transactionId(transaction.getId())
                .code(transaction.getCode())
                .driverId(transaction.getDriver().getId())
                .vehicleId(transaction.getVehicle().getId())
                .stationId(transaction.getStation().getId())
                .batteryTransactionResponses(responses)
                .confirmedStaffId(transaction.getConfirmedBy().getId())
                .scheduledTime(transaction.getScheduledTime())
                .arrivalTime(transaction.getArrivalTime())
                .swapStartTime(transaction.getSwapStartTime())
                .swapEndTime(transaction.getSwapEndTime())
                .status(transaction.getStatus())
                .type(transaction.getType())
                .swapPrice(transaction.getSwapPrice().getAmount())
                .notes(transaction.getNotes())
                .driverRating(transaction.getDriverRating())
                .driverFeedback(transaction.getDriverFeedback())
                .build();
    }
}
