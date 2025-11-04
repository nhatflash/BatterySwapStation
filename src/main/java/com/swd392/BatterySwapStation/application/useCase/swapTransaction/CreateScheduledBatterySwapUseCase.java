package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.common.shared.PriceCalculator;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateScheduledBatterySwapUseCase implements IUseCase<CreateScheduledBatterySwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final BatteryService batteryService;
    private final StationService stationService;
    private final UserService userService;
    private final VehicleService vehicleService;


    @Override
    @Transactional
    public SwapTransaction execute(CreateScheduledBatterySwapCommand request) {
        User requestDriver = swapTransactionService.getValidDriver(request.getDriverId());
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
        return swapTransactionService.saveSwapTransaction(newScheduledTransaction);
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
