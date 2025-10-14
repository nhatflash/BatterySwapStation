package com.swd392.BatterySwapStation.application.common.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String message;
    private T data;
    private long timestamp;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
