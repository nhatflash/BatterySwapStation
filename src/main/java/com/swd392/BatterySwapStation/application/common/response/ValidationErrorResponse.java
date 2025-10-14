package com.swd392.BatterySwapStation.application.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(String code, String message, Map<String, String> errors) {
        super(code, message);
        this.errors = errors;

    }
}
