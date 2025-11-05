package com.swd392.BatterySwapStation.application.service.business;


public interface IServiceProviders {
    IBatteryService getBatteryService();
    IBatterySimulatorService getBatterySimulatorService();
    IBatterySSEService getBatterySSEService();
    IDashboardService getDashboardService();
    IPaymentService getPaymentService();
    IStationService getStationService();
    IStationStaffService getStationStaffService();
    ISwapTransactionService  getSwapTransactionService();
    IUserService getUserService();
    IVehicleService getVehicleService();
    IVnPayService getVnPayService();
}
