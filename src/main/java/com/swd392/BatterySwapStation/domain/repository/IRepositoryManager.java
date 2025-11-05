package com.swd392.BatterySwapStation.domain.repository;

public interface IRepositoryManager {

    BatteryModelRepository getBatteryModelRepository();

    BatteryRepository getBatteryRepository();

    BatteryTransactionRepository getBatteryTransactionRepository();

    PaymentRepository getPaymentRepository();

    StationRepository getStationRepository();

    StationStaffRepository getStationStaffRepository();

    SwapTransactionRepository getSwapTransactionRepository();

    UserRepository getUserRepository();

    VehicleRepository getVehicleRepository();
}
