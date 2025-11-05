package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ISwapTransactionService {
    SwapTransaction createScheduledTransaction(User driver,
                                               Vehicle vehicle,
                                               Station station,
                                               LocalDateTime scheduledTime,
                                               String notes);

    SwapTransaction createWalkInTransaction(User driver,
                                            Vehicle vehicle,
                                            Station station);


    SwapTransaction getTransactionById(UUID transactionId);


    SwapTransaction getLatestCompletedVehicleTransaction(Vehicle vehicle);

    boolean isVehicleFirstSwap(Vehicle vehicle);

    List<BatteryTransaction> getBatteryTransactionFromTransaction(SwapTransaction transaction);

    List<SwapTransaction> GetUnconfirmedSwapTransaction(Station station);

    SwapTransaction saveSwapTransaction(SwapTransaction swapTransaction);

    User getValidStaff(UUID staffId);
    Station getValidStationFromStaffId(UUID staffId);
    List<Battery> getRequestedNewBatteries(List<UUID> newBatteryIds, UUID stationId, String vehicleBatteryType, List<Battery> oldBatteries);
    void checkVehicleIsAllowedForSwap(Vehicle vehicle);
    Battery getValidNewBatteryForSwapping(UUID batteryId, UUID stationId, String vehicleBatteryType);
    void addOldBatteryTransactionIfExists(Vehicle vehicle, SwapTransaction swapTransaction);
    User getValidDriver(UUID driverId);
    Vehicle getValidVehicle(UUID vehicleId, User driver);
    List<Battery> getOldBatteryInVehicle(Vehicle vehicle);
    List<SwapTransaction> getAllSwapForVehicle(Vehicle vehicle);
    int countByStationAndSwapEndTimeIsNotNull(Station station);
    List<SwapTransaction> findByStationAndSwapEndTimeIsNotNull(Station station);
    List<SwapTransaction> findByDriverAndStatus(User driver, TransactionStatus status);
    BatteryTransaction findLatestBatteryTransactionWithNewBattery(Battery newBattery);
    BatteryTransaction findLatestBatteryTransactionWithOldBattery(Battery oldBattery);
    void checkBatteryIsReadyForSwapping(Battery battery);

}
