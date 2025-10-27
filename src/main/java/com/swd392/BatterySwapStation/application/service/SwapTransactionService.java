package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.application.common.shared.PriceCalculator;
import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.domain.enums.UserStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.infrastructure.repository.BatteryTransactionRepository;
import com.swd392.BatterySwapStation.infrastructure.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SwapTransactionService {

    private final SwapTransactionRepository swapTransactionRepository;
    private final BatteryTransactionRepository batteryTransactionRepository;
    private final UserService userService;
    private final StationStaffService stationStaffService;
    private final BatteryService batteryService;
    private final PaymentService paymentService;
    private final VehicleService vehicleService;

    public SwapTransactionService(SwapTransactionRepository swapTransactionRepository,
                                  BatteryTransactionRepository batteryTransactionRepository,
                                  UserService userService,
                                  StationStaffService stationStaffService,
                                  BatteryService batteryService,
                                  PaymentService paymentService,
                                  VehicleService vehicleService) {
        this.swapTransactionRepository = swapTransactionRepository;
        this.batteryTransactionRepository = batteryTransactionRepository;
        this.userService = userService;
        this.stationStaffService = stationStaffService;
        this.batteryService = batteryService;
        this.paymentService = paymentService;
        this.vehicleService = vehicleService;

    }

    public SwapTransaction createScheduledTransaction(User driver,
                                                      Vehicle vehicle,
                                                      Station station,
                                                      LocalDateTime scheduledTime,
                                                      String notes) {
        var newSwapTransaction = SwapTransaction.builder()
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
        return saveSwapTransaction(newSwapTransaction);
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
        var swapTransactions = swapTransactionRepository.findAllByVehicleOrderByIdDesc(vehicle, TransactionStatus.COMPLETED);
        if (swapTransactions.isEmpty()) {
            return null;
        }
        return swapTransactions.getFirst();
    }

    public boolean isVehicleFirstSwap(Vehicle vehicle) {
        SwapTransaction latestVehicleTransaction = getLatestCompletedVehicleTransaction(vehicle);
        return latestVehicleTransaction == null;
    }

    public Set<BatteryTransaction> getBatteryTransactionFromTransaction(SwapTransaction transaction) {
        return batteryTransactionRepository.findAllBySwapTransaction(transaction);
    }

    public List<SwapTransaction> GetUnconfirmedSwapTransaction(Station station) {
        return swapTransactionRepository.findAllByStation(station);
    }


    public SwapTransaction saveSwapTransaction(SwapTransaction swapTransaction) {
        return swapTransactionRepository.save(swapTransaction);
    }

    public SwapTransaction getValidSwapTransaction(UUID transactionId) {
        var transaction = getTransactionById(transactionId);
        checkValidTransactionForSwapping(transaction);
        return transaction;
    }

    public User getValidStaff(UUID staffId) {
        var staff = userService.getUserById(staffId);
        if (staff.getStatus() != UserStatus.ACTIVE || !staff.getRole().equals(UserRole.STAFF)) {
            throw new IllegalArgumentException("This staff member is not active or is not a staff member.");
        }
        return staff;
    }

    public Station getValidStation(UUID staffId) {
        var stationStaff = stationStaffService.getStationStaffById(staffId);
        var station = stationStaff.getStation();
        checkValidStation(station);
        return station;
    }

    public List<Battery> getRequestedNewBatteries(List<UUID> newBatteryIds, UUID stationId, String vehicleBatteryType) {
        if (newBatteryIds == null || newBatteryIds.isEmpty()) {
            throw new IllegalArgumentException("New batteries for swapping needs to be specified.");
        }
        List<Battery> newBatteries = new ArrayList<>();
        for (var newBatteryId : newBatteryIds) {
            newBatteries.add(getValidNewBatteryForSwapping(newBatteryId, stationId, vehicleBatteryType));
        }
        return newBatteries;
    }

    public Battery getValidNewBatteryForSwapping(UUID batteryId, UUID stationId, String vehicleBatteryType) {
        Battery battery = batteryService.findByBatteryId(batteryId);
        checkValidRequestBatterySwapping(battery, stationId, vehicleBatteryType);
        return battery;
    }

    public void addOldBatteryTransactionIfExists(Vehicle vehicle, SwapTransaction swapTransaction) {
        List<Battery> oldVehicleBatteries = getOldBatteryInVehicle(vehicle);
        if (!oldVehicleBatteries.isEmpty()) {
            List<BatteryTransaction> batteryTransactions = swapTransaction.getBatteryTransactions();
            for (Battery oldBattery : oldVehicleBatteries) {
                batteryTransactions.add(
                        BatteryTransaction.builder()
                                .oldBattery(oldBattery)
                                .swapTransaction(swapTransaction)
                                .build()
                );
            }
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
            if (latestCompletedBatteryTransaction.getOldBattery() == null) {
                continue;
            }
            oldBatteriesInVehicle.add(latestCompletedBatteryTransaction.getOldBattery());
        }
        return oldBatteriesInVehicle;
    }

    public List<SwapTransaction> getAllSwapForVehicle(Vehicle vehicle) {
        return swapTransactionRepository.findAllByVehicle(vehicle);
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

    private void checkValidTransactionForSwapping(SwapTransaction transaction) {
        if (!transaction.isTransactionNotConfirmedBy() || !transaction.isTransactionScheduled()) {
            throw new IllegalArgumentException("Transaction has already been confirmed or it is not scheduled.");
        }
        if (transaction.isTransactionExpired()) {
            throw new IllegalArgumentException("Transaction has expired.");
        }
        List<Payment> payments = paymentService.findAllWithOrderDescByTransactionId(transaction);
        if (payments != null && !payments.isEmpty()) {
            Payment latestPayment = payments.getFirst();
            if (latestPayment.isPaymentCompleted()) {
                throw new IllegalArgumentException("Payment has already been completed.");
            }
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
