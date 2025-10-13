package com.swd392.BatterySwapStation.application.common;

import lombok.Data;

@Data
public class ErrorResponse {
    private String code;
    private String message;
    private long timestamp;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
