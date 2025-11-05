package com.swd392.BatterySwapStation.infrastructure.service.business;

import com.swd392.BatterySwapStation.application.common.shared.PriceCalculator;
import com.swd392.BatterySwapStation.application.service.business.ISwapTransactionService;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.BatteryTransactionRepository;
import com.swd392.BatterySwapStation.domain.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SwapTransactionService implements ISwapTransactionService {

    private final SwapTransactionRepository swapTransactionRepository;
    private final BatteryTransactionRepository batteryTransactionRepository;
    private final UserService userService;
    private final StationStaffService stationStaffService;
    private final BatteryService batteryService;
    private final VehicleService vehicleService;

    public SwapTransaction createScheduledTransaction(User driver,
                                                      Vehicle vehicle,
                                                      Station station,
                                                      LocalDateTime scheduledTime,
                                                      String notes) {
        return SwapTransaction.builder()
                .code(generateTransactionCode())
                .driver(driver)
                .vehicle(vehicle)
                .station(station)
                .scheduledTime(scheduledTime)
                .expiredTime(scheduledTime.plusDays(3))
                .status(TransactionStatus.SCHEDULED)
                .type(SwapType.SCHEDULED)
                .swapPrice(new Money(BigDecimal.valueOf(vehicle.getBatteryCapacity() * PriceCalculator.SWAP_PRICE)))
                .notes(notes)
                .build();

    }

    public SwapTransaction createWalkInTransaction(User driver,
                                                   Vehicle vehicle,
                                                   Station station) {
        return SwapTransaction.builder()
                .code(generateTransactionCode())
                .driver(driver)
                .vehicle(vehicle)
                .station(station)
                .status(TransactionStatus.CONFIRMED)
                .swapPrice(new Money(BigDecimal.valueOf(vehicle.getBatteryCapacity() * PriceCalculator.SWAP_PRICE)))
                .type(SwapType.WALK_IN)
                .build();
    }


    public SwapTransaction getTransactionById(UUID transactionId) {
        return swapTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
    }

    public SwapTransaction getLatestCompletedVehicleTransaction(Vehicle vehicle) {
        var swapTransactions = swapTransactionRepository.findAllByVehicle(vehicle, TransactionStatus.COMPLETED);
        if (swapTransactions.isEmpty()) {
            return null;
        }
        return swapTransactions.getLast();
    }

    public boolean isVehicleFirstSwap(Vehicle vehicle) {
        SwapTransaction latestVehicleTransaction = getLatestCompletedVehicleTransaction(vehicle);
        return latestVehicleTransaction == null;
    }

    public List<BatteryTransaction> getBatteryTransactionFromTransaction(SwapTransaction transaction) {
        return batteryTransactionRepository.findAllBySwapTransaction(transaction);
    }

    public List<SwapTransaction> GetUnconfirmedSwapTransaction(Station station) {
        return swapTransactionRepository.findAllByStation(station);
    }


    public SwapTransaction saveSwapTransaction(SwapTransaction swapTransaction) {
        return swapTransactionRepository.save(swapTransaction);
    }


    public User getValidStaff(UUID staffId) {
        var staff = userService.getUserById(staffId);
        if (staff.getStatus() != UserStatus.ACTIVE || !staff.getRole().equals(UserRole.STAFF)) {
            throw new IllegalArgumentException("This staff member is not active or is not a staff member.");
        }
        return staff;
    }

    public Station getValidStationFromStaffId(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        var station = stationStaff.getStation();
        checkValidStation(station);
        return station;
    }

    public List<Battery> getRequestedNewBatteries(List<UUID> newBatteryIds, UUID stationId, String vehicleBatteryType, List<Battery> oldBatteries) {
        if (newBatteryIds == null || newBatteryIds.isEmpty()) {
            throw new IllegalArgumentException("New batteries for swapping needs to be specified.");
        }
        List<Battery> newBatteries = new ArrayList<>();
        for (var newBatteryId : newBatteryIds) {
            for (var oldBattery : oldBatteries) {
                if (newBatteryId.equals(oldBattery.getId())) {
                    throw new IllegalArgumentException("The chosen battery is already in used: " + newBatteryId);
                }
            }
            newBatteries.add(getValidNewBatteryForSwapping(newBatteryId, stationId, vehicleBatteryType));
        }
        return newBatteries;
    }

    public void checkVehicleIsAllowedForSwap(Vehicle vehicle) {
        List<SwapTransaction> vehicleTransactions = getAllSwapForVehicle(vehicle);
        if (vehicleTransactions.isEmpty()) {
            return;
        }
        SwapTransaction latestVehicleTransaction = vehicleTransactions.getLast();
        if (!latestVehicleTransaction.isTransactionCompleted() && !latestVehicleTransaction.isTransactionExpired() && !latestVehicleTransaction.isTransactionCanceled()) {
            throw new IllegalArgumentException("This vehicle has already contained an unsuccessful swap transaction. Cannot make another swap transaction.");
        }
    }

    public Battery getValidNewBatteryForSwapping(UUID batteryId, UUID stationId, String vehicleBatteryType) {
        Battery battery = batteryService.findByBatteryId(batteryId);
        checkValidRequestBatterySwapping(battery, stationId, vehicleBatteryType);
        return battery;
    }

    public void addOldBatteryTransactionIfExists(Vehicle vehicle, SwapTransaction swapTransaction) {
        List<Battery> oldVehicleBatteries = getOldBatteryInVehicle(vehicle);
        if (!oldVehicleBatteries.isEmpty()) {
            List<BatteryTransaction> batteryTransactions = new ArrayList<>();
            for (Battery oldBattery : oldVehicleBatteries) {
                batteryTransactions.add(
                        BatteryTransaction.builder()
                                .oldBattery(oldBattery)
                                .swapTransaction(swapTransaction)
                                .build()
                );
            }
            swapTransaction.setBatteryTransactions(batteryTransactions);
            swapTransaction.setInitialSwap(false);
            return;
        }
        swapTransaction.setInitialSwap(true);
    }

    public User getValidDriver(UUID driverId) {
        User driver = userService.getUserById(driverId);
        if (!userService.isCorrectRole(driver, UserRole.DRIVER)) {
            throw new IllegalArgumentException("Only driver can perform this operation.");
        }
        return driver;
    }

    public Vehicle getValidVehicle(UUID vehicleId, User driver) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (!vehicle.getDriver().equals(driver)) {
            throw new IllegalArgumentException("This vehicle is not assigned to this driver.");
        }
        return vehicle;
    }


    public List<Battery> getOldBatteryInVehicle(Vehicle vehicle) {
        SwapTransaction latestCompletedTransaction = getLatestCompletedVehicleTransaction(vehicle);
        if (latestCompletedTransaction == null) {
            return new ArrayList<>();
        }
        var latestCompletedBatteryTransactions = latestCompletedTransaction.getBatteryTransactions();
        if (latestCompletedBatteryTransactions == null || latestCompletedBatteryTransactions.isEmpty()) {
            return new ArrayList<>();
        }
        List<Battery> oldBatteriesInVehicle = new ArrayList<>();
        for (var latestCompletedBatteryTransaction : latestCompletedBatteryTransactions) {
            oldBatteriesInVehicle.add(latestCompletedBatteryTransaction.getNewBattery());
        }
        return oldBatteriesInVehicle;
    }

    public List<SwapTransaction> getAllSwapForVehicle(Vehicle vehicle) {
        return swapTransactionRepository.findAllByVehicle(vehicle);
    }

    public int countByStationAndSwapEndTimeIsNotNull(Station station) {
        return swapTransactionRepository.countByStationAndSwapEndTimeIsNotNull(station);
    }

    public List<SwapTransaction> findByStationAndSwapEndTimeIsNotNull(Station station) {
        return swapTransactionRepository.findByStationAndSwapEndTimeIsNotNull(station);
    }

    public List<SwapTransaction> findByDriverAndStatus(User driver, TransactionStatus status) {
        return swapTransactionRepository.findByDriverAndStatus(driver, status);
    }

    public BatteryTransaction findLatestBatteryTransactionWithNewBattery(Battery newBattery) {
        List<BatteryTransaction> batteryTransactions = batteryTransactionRepository.findByNewBatteryOrderByIdDesc(newBattery);
        if (batteryTransactions.isEmpty()) {
            return null;
        }
        return batteryTransactions.getFirst();
    }

    public BatteryTransaction findLatestBatteryTransactionWithOldBattery(Battery oldBattery) {
        List<BatteryTransaction> batteryTransactions = batteryTransactionRepository.findByOldBatteryOrderByIdDesc(oldBattery);
        if (batteryTransactions.isEmpty()) {
            return null;
        }
        return batteryTransactions.getFirst();
    }

    public void checkBatteryIsReadyForSwapping(Battery battery) {
        BatteryTransaction newTransaction = findLatestBatteryTransactionWithNewBattery(battery);
        BatteryTransaction oldTransaction = findLatestBatteryTransactionWithOldBattery(battery);
        if (newTransaction != null && oldTransaction != null) {
            if (newTransaction.getId() > oldTransaction.getId()) {
                throw new IllegalArgumentException("The battery " + battery.getId() + " is already on another swap or is on another vehicle.");
            }
        } else if (oldTransaction == null && newTransaction != null) {
            throw new IllegalArgumentException("The battery " + battery.getId() + " is already on another swap or is on another vehicle..");
        }
    }


    private void checkValidStation(Station station) {
        if (station.isCurrentCapacityEmpty()) {
            throw new IllegalArgumentException("Station currently has no battery.");
        }
        if (!station.isOperational()) {
            throw new IllegalArgumentException("This station is not operational.");
        }
        if (!station.isOnWorkingTime()) {
            throw new IllegalArgumentException("This station is not on working time.");
        }
    }


    private void checkValidRequestBatterySwapping(Battery battery, UUID stationId, String vehicleBatteryType) {
        if (!battery.isOnStation(stationId)) {
            throw new IllegalArgumentException("Battery is not assigned to this station: " + battery.getId());
        }
        if (!battery.isBatteryFull()) {
            throw new IllegalArgumentException("Battery is not full: " + battery.getId());
        }
        if (!battery.isMatchType(vehicleBatteryType)) {
            throw new IllegalArgumentException("Battery is not match the type for the request vehicle: " + battery.getId());
        }
    }



    private boolean isRequestBatteryCountMatchVehicleBatteryCapacity(int vehicleBatteryCapacity, int requestedBatteryCount) {
        return vehicleBatteryCapacity == requestedBatteryCount;
    }


    private String generateTransactionCode() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        int randomCode = new Random().nextInt(99999999);
        return String.format("TXN-%s-%08d", LocalDateTime.now().format(dtf),
                randomCode);
    }
}
