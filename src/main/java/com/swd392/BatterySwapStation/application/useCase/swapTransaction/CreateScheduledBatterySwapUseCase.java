package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.service.*;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.stereotype.Service;

@Service
public class CreateScheduledBatterySwapUseCase implements IUseCase<CreateScheduledBatterySwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final BatteryService batteryService;
    private final StationService stationService;
    private final UserService userService;
    private final VehicleService vehicleService;

    public CreateScheduledBatterySwapUseCase(SwapTransactionService swapTransactionService,
                                             BatteryService batteryService,
                                             StationService stationService,
                                             UserService userService,
                                             VehicleService vehicleService) {
        this.swapTransactionService = swapTransactionService;
        this.batteryService = batteryService;
        this.stationService = stationService;
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @Override
    public SwapTransaction execute(CreateScheduledBatterySwapCommand request) {
        User driver = userService.getUserById(request.getDriverId());
        if (!userService.isCorrectRole(driver, UserRole.DRIVER)) {
            throw new IllegalArgumentException("Only driver can perform this operation.");
        }

        Station station = stationService.getByStationID(request.getStationId());
        if (!stationService.isStationOperational(station)) {
            throw new IllegalArgumentException("This station is not operating.");
        }

        BatteryModel model = batteryService.findByBatteryType(request.getBatteryType());
        int availableQuantity = batteryService.countByCurrentStationAndModel(request.getStationId(), request.getBatteryType());
        if (availableQuantity < request.getQuantity()) {
            throw new IllegalArgumentException("This battery type on the station is insufficient.");
        }

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleService.getVehicleById(request.getVehicleId());
            if (!isValidVehicleForSwapping(vehicle, request.getBatteryType(), driver)) {
                throw new IllegalArgumentException("Invalid request vehicle for battery swapping.");
            }
        }



        return null;
    }

    private boolean isValidVehicleForSwapping(Vehicle vehicle, String batteryType, User driver) {
        return vehicleService.isVehicleMatchesRequestBatteryType(vehicle, new BatteryType(batteryType))
                 && vehicleService.isVehicleBelongToUser(driver, vehicle);
    }


}
