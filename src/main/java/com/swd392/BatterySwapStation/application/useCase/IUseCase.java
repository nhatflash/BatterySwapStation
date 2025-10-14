package com.swd392.BatterySwapStation.application.useCase;

public interface IUseCase<TRequest, TResponse> {
   TResponse execute(TRequest request);
}
