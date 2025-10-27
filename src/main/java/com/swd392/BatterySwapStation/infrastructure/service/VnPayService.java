package com.swd392.BatterySwapStation.infrastructure.service;

import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import com.swd392.BatterySwapStation.domain.enums.PaymentStatus;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.infrastructure.repository.PaymentRepository;
import com.swd392.BatterySwapStation.infrastructure.repository.SwapTransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class VnPayService {

    @Value("${vnPay.vnp_TmnCode}")
    private String vnpTmnCode;

    @Value("${vnPay.vnp_HashSecret}")
    private String vnpHashSecret;

    @Value("${vnPay.vnp_Url}")
    private String vnpUrl;

    @Value("${vnPay.vnp_BaseUrl}")
    private String vnpBaseUrl;

    private final SwapTransactionRepository swapTransactionRepository;
    private final PaymentRepository paymentRepository;

    public VnPayService(SwapTransactionRepository swapTransactionRepository, PaymentRepository paymentRepository) {
        this.swapTransactionRepository = swapTransactionRepository;
        this.paymentRepository = paymentRepository;
    }

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
        final String vnpReturnUrl = vnpBaseUrl + "/vnpay-return";

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
        vnpParams.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
        vnpParams.put("vnp_CreateDate", createdDate);
        vnpParams.put("vnp_ExpireDate", expiredDate);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_IpAddr",  remoteIpAddress);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_TxnRef", transaction.getId() + "-" + System.currentTimeMillis());
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang");
        vnpParams.put("vnp_OrderType", transaction.getType().name());
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);

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




    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        return hmacSha512(vnpHashSecret, hashData.toString());
    }

    public boolean isValidSignature(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
            String fieldName = e.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signValue = hashAllFields(fields);
        return signValue.equals(vnpSecureHash);
    }

    public String processIPN(HttpServletRequest request) {
        String response = "";
        if (!isValidSignature(request)) {
            log.error("Invalid signature from VnPay IPN.");
            response = "Invalid signature from VnPay IPN.";
            return response;
        }
        log.info("Signature verify successfully. Processing payment update...");

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        UUID transactionId = getTransactionIdFromTxnRef(txnRef);
        SwapTransaction transaction = swapTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found with ID: " + transactionId));

        if ("00".equals(responseCode)) {
            log.info("Processing payment update successful.");
            var payment = updateSuccessfulPayment(transaction);
            response = payment.getId().toString();
        } else {
            log.info("Processing payment update failed.");
            updateFailedPayment(transaction);
            response = "Processing payment update failed.";
        }
        return response;
    }


    private UUID getTransactionIdFromTxnRef(String txnRef) {
        var idString = txnRef.substring(0, 36);
        return UUID.fromString(idString);
    }

    private boolean isTransactionExists(UUID transactionId) {
        return swapTransactionRepository.existsSwapTransactionById(transactionId);
    }

    private Payment updateSuccessfulPayment(SwapTransaction transaction) {
        var payment = Payment.builder()
                .swapTransaction(transaction)
                .amount(transaction.getSwapPrice())
                .method(PaymentMethod.VNPAY)
                .status(PaymentStatus.COMPLETED)
                .notes("Payment with VnPay.")
                .build();
        var savedPayment = paymentRepository.save(payment);
        transaction.setStatus(TransactionStatus.COMPLETED);
        swapTransactionRepository.save(transaction);
        return savedPayment;
    }

    private void updateFailedPayment(SwapTransaction transaction) {
        var payment = Payment.builder()
                .swapTransaction(transaction)
                .amount(transaction.getSwapPrice())
                .method(PaymentMethod.VNPAY)
                .status(PaymentStatus.FAILED)
                .notes("Payment with VnPay.")
                .build();
        paymentRepository.save(payment);
    }

}
