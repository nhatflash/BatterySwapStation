package com.swd392.BatterySwapStation.infrastructure.repository;

import com.swd392.BatterySwapStation.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepositoryManager implements IRepositoryManager {

    private final BatteryModelRepository batteryModelRepository;
    private final BatteryRepository batteryRepository;
    private final BatteryTransactionRepository batteryTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final StationRepository stationRepository;
    private final StationStaffRepository stationStaffRepository;
    private final SwapTransactionRepository swapTransactionRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public BatteryModelRepository getBatteryModelRepository() {
        return batteryModelRepository;
    }

    @Override
    public BatteryRepository getBatteryRepository() {
        return batteryRepository;
    }

    @Override
    public BatteryTransactionRepository getBatteryTransactionRepository() {
        return batteryTransactionRepository;
    }

    @Override
    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    @Override
    public StationRepository getStationRepository() {
        return stationRepository;
    }

    @Override
    public StationStaffRepository getStationStaffRepository() {
        return stationStaffRepository;
    }

    @Override
    public SwapTransactionRepository getSwapTransactionRepository() {
        return swapTransactionRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }
}
