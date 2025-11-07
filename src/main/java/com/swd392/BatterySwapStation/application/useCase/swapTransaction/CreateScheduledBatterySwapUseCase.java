package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.common.shared.PriceCalculator;
import com.swd392.BatterySwapStation.application.model.command.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.application.service.business.IStationService;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import com.swd392.BatterySwapStation.domain.model.AuthenticatedUser;
import com.swd392.BatterySwapStation.application.security.ICurrentAuthenticatedUser;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.SwapTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateScheduledBatterySwapUseCase implements IUseCase<CreateScheduledBatterySwapCommand, SwapTransactionResponse> {

    private final ISwapTransactionService swapTransactionService;
    private final IBatteryService batteryService;
    private final IStationService stationService;
    private final ICurrentAuthenticatedUser currentAuthenticatedUser;


    @Override
    @Transactional
    public SwapTransactionResponse execute(CreateScheduledBatterySwapCommand request) {
        AuthenticatedUser authenticatedUser = currentAuthenticatedUser.getCurrentAuthenticatedUser();
        User requestDriver = swapTransactionService.getValidDriver(authenticatedUser.getUserId());
        Vehicle requestedVehicle = swapTransactionService.getValidVehicle(request.getVehicleId(), requestDriver);
        swapTransactionService.checkVehicleIsAllowedForSwap(requestedVehicle);
        BatteryType requestedBatteryType = requestedVehicle.getBatteryType();
        Station station = getValidStation(request.getStationId(),
                requestedBatteryType,
                requestedVehicle.getBatteryCapacity());
        SwapTransaction newScheduledTransaction = swapTransactionService.createScheduledTransaction(
                requestDriver,
                requestedVehicle,
                station,
                getValidScheduledDateTime(request.getScheduledTime()),
                request.getNotes()
        );
        if (swapTransactionService.isVehicleFirstSwap(requestedVehicle)) {
            newScheduledTransaction.setSwapPrice(
                    new Money(BigDecimal.valueOf(PriceCalculator.FIRST_SWAP_PRICE * requestedVehicle.getBatteryCapacity())));
        }
        swapTransactionService.addOldBatteryTransactionIfExists(requestedVehicle, newScheduledTransaction);
        SwapTransaction savedTransaction = swapTransactionService.saveSwapTransaction(newScheduledTransaction);
        return ResponseMapper.mapToSwapTransactionResponse(savedTransaction);
    }


    private Station getValidStation(UUID stationId, BatteryType batteryType, int vehicleBatteryCapacity) {
        Station station = stationService.getByStationID(stationId);
        if (!stationService.isStationOperational(station)) {
            throw new IllegalArgumentException("This station is not operating.");
        }
        if (!isStationHasEnoughBatteryType(station, batteryType, vehicleBatteryCapacity)) {
            throw new IllegalArgumentException("This station does not have enough this battery type.");
        }
        return station;
    }

    private boolean isStationHasEnoughBatteryType(Station station, BatteryType batteryType, int vehicleBatteryCapacity) {
        int currentCapacity = batteryService.countByCurrentStationAndModel(station.getId(), batteryType.getValue());
        return currentCapacity >= vehicleBatteryCapacity;
    }



    private LocalDateTime getValidScheduledDateTime(String scheduledTime) {
        LocalDateTime scheduledDateTime = DateStringMapper.getLocalDateTime(scheduledTime);
        if (scheduledDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The scheduled date cannot be in the past.");
        }
        return scheduledDateTime;
    }

}
