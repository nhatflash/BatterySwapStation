package com.swd392.BatterySwapStation.application.useCase.swapTransaction;

import com.swd392.BatterySwapStation.application.model.CreateWalkInSwapCommand;
import com.swd392.BatterySwapStation.application.service.SwapTransactionService;
import com.swd392.BatterySwapStation.application.service.UserService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CreateWalkInSwapUseCase implements IUseCase<CreateWalkInSwapCommand, SwapTransaction> {

    private final SwapTransactionService swapTransactionService;
    private final UserService userService;

    public CreateWalkInSwapUseCase(SwapTransactionService swapTransactionService,
                                   UserService userService) {
        this.swapTransactionService = swapTransactionService;
        this.userService = userService;
    }

    @Override
    public SwapTransaction execute(CreateWalkInSwapCommand request) {
        User staff = swapTransactionService.getValidStaff(request.getStaffId());
        User driver = swapTransactionService.getValidDriver(request.getDriverId());
        Vehicle vehicle = swapTransactionService.getValidVehicle(request.getVehicleId(), driver);
        var station = swapTransactionService.getValidStation(request.getStaffId());
        List<Battery> vehicleOldBatteries = swapTransactionService.getOldBatteryInVehicle(vehicle);
        List<Battery> requestedNewBatteries = swapTransactionService.getRequestedNewBatteries(request.getBatteryIds(),
                station.getId(),
                vehicle.getBatteryType().getValue());
        return createWalkInTransaction(staff, vehicle, requestedNewBatteries, vehicleOldBatteries);
    }

    private SwapTransaction createWalkInTransaction(User staff, Vehicle vehicle, List<Battery> requestedNewBatteries, List<Battery> oldVehicleBatteries) {
        return null;
    }



}
