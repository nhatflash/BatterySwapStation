package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface IVnPayService {
    String hmacSha512(String key, String data);
    Map<String, String> getVnpParams(HttpServletRequest request, SwapTransaction transaction);
    String getQueryUrl(Map<String, String> vnpParams);
    boolean isValidSignature(HttpServletRequest request);
    String processIPN(HttpServletRequest request);

}
