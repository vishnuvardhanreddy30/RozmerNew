package com.rozmer.service.external.payments;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CashfreeAuthService {
    @Value("${cashfree.api.url}")
    private String cashfreeApiUrl;
    @Value("${cashfree.app.id}")
    private String appId;
    @Value("${cashfree.key}")
    private String secretKey;

    @Autowired
    private RestTemplate restTemplate;

    public Object submitOrResendOtp(OtpRequest request) {
        String url = cashfreeApiUrl + "/otp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<OtpRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to submit or resend OTP with Cashfree");
    }
}
