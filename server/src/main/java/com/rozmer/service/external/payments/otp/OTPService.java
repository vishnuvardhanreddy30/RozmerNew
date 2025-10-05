package com.rozmer.service.external.payments.otp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class OTPService {

    private final String apiUrl = "https://sandbox.cashfree.com/pg/orders/pay/authenticate/{cf_payment_id}";
    private final String apiVersion = "2023-08-01";
    private final String apiKey = "your_api_key_here"; // Replace with your actual API key

    public ResponseEntity<String> authenticateOTP(String cfPaymentId, OTPRequest otpRequest) {
        String url = apiUrl.replace("{cf_payment_id}", cfPaymentId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", apiVersion);
        headers.set("x-request-id", "unique-request-id"); // Optional
        headers.set("x-idempotency-key", "unique-idempotency-key"); // Optional
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<OTPRequest> entity = new HttpEntity<>(otpRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}

