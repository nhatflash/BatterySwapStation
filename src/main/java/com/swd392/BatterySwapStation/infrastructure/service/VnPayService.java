package com.swd392.BatterySwapStation.infrastructure.service;

import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
public class VnPayService {

    @Value("${vnPay.vnp_TmnCode}")
    private String vnpTmnCode;

    @Value("${vnPay.vnp_HashSecret}")
    private String vnpHashSecret;

    @Value("${vnPay.vnp_Url}")
    private String vnpUrl;

    public String hmacSha512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NotFoundException("Key or data are missing.");
            }
            final Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            byte[] keyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA512");
            hmacSha512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmacSha512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String, String> getVnpParams(HttpServletRequest request, SwapTransaction transaction) {
        final String vnpVersion = "2.1.0";
        final String vnpCommand = "pay";
        final String vnpReturnUrl = "https://czf23bx8-8080.asse.devtunnels.ms/vnpay-return";
        final String vnpIpnUrl = "https://czf23bx8-8080.asse.devtunnels.ms/vnpay-ipn";

        String remoteIpAddress = request.getHeader("X-Forwarded-For");
        if (remoteIpAddress == null) {
            remoteIpAddress = request.getRemoteAddr();
        }

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Etc/GMT+7")));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String expiredDate = formatter.format(cld.getTime());

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);

        BigDecimal amount = transaction.getSwapPrice().getAmount();
        vnpParams.put("vnp_Amount", String.valueOf(amount.doubleValue() * 100));
        vnpParams.put("vnp_CreateDate", createdDate);
        vnpParams.put("vnp_ExpireDate", expiredDate);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_IpAddr",  remoteIpAddress);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_TxnRef", UUID.randomUUID() + "-" + System.currentTimeMillis());
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang");
        vnpParams.put("vnp_OrderType", transaction.getType().name());
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpnUrl", vnpIpnUrl);

        return vnpParams;
    }

    public String getQueryUrl(Map<String, String> vnpParams) {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnpSecureHash = hmacSha512(vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnpUrl + "?" + queryUrl;
    }

}
