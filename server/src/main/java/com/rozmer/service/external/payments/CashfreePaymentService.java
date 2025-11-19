package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.rozmer.service.entity.Payment;
import com.rozmer.service.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CashfreePaymentService {

    @Value("${cashfree.api.url}")
    private String cashfreeApiUrl;

    @Value("${cashfree.app.id}")
    private String appId;

    @Value("${cashfree.key}")
    private String secretKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    public Map<String, Object> createOrder(String orderId, String orderAmount, String customerPhone, String customerEmail, String returnUrl) {
        String url = cashfreeApiUrl + "/orders";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("appId", appId);
        requestBody.put("orderId", orderId);
        requestBody.put("orderAmount", orderAmount);
        requestBody.put("orderCurrency", "INR");
        requestBody.put("customerPhone", customerPhone);
        requestBody.put("customerEmail", customerEmail);
        requestBody.put("returnUrl", returnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBodyObj = response.getBody();
            if (responseBodyObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) responseBodyObj;
                // Extract payment details from response and save
                if (responseBody.containsKey("paymentId")) {
                    Payment payment = new Payment();
                    payment.setPaymentId((String) responseBody.get("paymentId"));
                    payment.setOrderId(orderId);
                    payment.setAmount(Double.parseDouble(orderAmount));
                    payment.setStatus((String) responseBody.getOrDefault("paymentStatus", "CREATED"));
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                }
                return responseBody;
            }
        }
        throw new RuntimeException("Failed to create order with Cashfree");
    }

    public Map<String, Object> getPaymentsForOrder(String orderId) {
        String url = cashfreeApiUrl + "/orders/" + orderId + "/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBodyObj = response.getBody();
            if (responseBodyObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) responseBodyObj;
                if (responseBody.containsKey("payments")) {
                    Object paymentsObj = responseBody.get("payments");
                    if (paymentsObj instanceof Iterable) {
                        for (Object paymentObj : (Iterable<?>) paymentsObj) {
                            if (paymentObj instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> paymentMap = (Map<String, Object>) paymentObj;
                                Payment payment = new Payment();
                                payment.setPaymentId((String) paymentMap.get("payment_id"));
                                payment.setOrderId(orderId);
                                Double amount = null;
                                if (paymentMap.get("amount") != null) {
                                    amount = Double.parseDouble(paymentMap.get("amount").toString());
                                }
                                if (amount != null) {
                                    payment.setAmount(amount);
                                }
                                payment.setStatus((String) paymentMap.get("status"));
                                payment.setPaidAt(LocalDateTime.now());
                                paymentRepository.save(payment);
                            }
                        }
                    }
                }
                return responseBody;
            }
        }
        throw new RuntimeException("Failed to get payments for order from Cashfree");
    }

    public Object getPaymentById(String paymentId) {
        // First try to get updated data from Cashfree API
        String url = cashfreeApiUrl + "/payments/" + paymentId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Object responseBodyObj = response.getBody();
                if (responseBodyObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseBody = (Map<String, Object>) responseBodyObj;
                    // Update/save the payment data
                    Payment payment = new Payment();
                    payment.setPaymentId(paymentId);
                    payment.setOrderId((String) responseBody.get("order_id"));
                    payment.setStatus((String) responseBody.get("status"));
                    if (responseBody.get("amount") != null) {
                        payment.setAmount(Double.parseDouble(responseBody.get("amount").toString()));
                    }
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                    return responseBody;
                }
            }
        } catch (Exception e) {
            // If API call fails, fall back to database
        }

        // Return from database as fallback
        return paymentRepository.findById(paymentId).orElse(null);
    }
}
