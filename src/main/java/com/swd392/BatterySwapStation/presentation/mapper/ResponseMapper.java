package com.swd392.BatterySwapStation.presentation.mapper;

import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.presentation.dto.response.*;

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
}
