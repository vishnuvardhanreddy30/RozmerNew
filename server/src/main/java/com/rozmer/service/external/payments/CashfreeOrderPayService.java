package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.rozmer.service.entity.Order;
import com.rozmer.service.repository.OrderRepository;
import com.rozmer.service.entity.Payment;
import com.rozmer.service.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CashfreeOrderPayService {

    @Value("${cashfree.api.payments.base-url:https://api.cashfree.com/pg}")
    private String baseUrl;

    @Value("${cashfree.api.version:2023-08-01}")
    private String apiVersion;

    @Value("${cashfree.app.id}")
    private String appId;

    @Value("${cashfree.secret.key}")
    private String secretKey;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public PayOrderResponse payOrder(PayOrderRequest request) {
        String url = baseUrl + "/orders/sessions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-version", apiVersion);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);

        HttpEntity<PayOrderRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PayOrderResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                PayOrderResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            PayOrderResponse payOrderResponse = response.getBody();
            // Persist payment/order data if available
            if (payOrderResponse != null && payOrderResponse.getOrderId() != null) {
                Order order = new Order();
                order.setOrderId(payOrderResponse.getOrderId());
                order.setStatus("PAID");
                order.setCreatedAt(LocalDateTime.now());
                orderRepository.save(order);
                if (payOrderResponse.getPaymentId() != null) {
                    Payment payment = new Payment();
                    payment.setPaymentId(payOrderResponse.getPaymentId());
                    payment.setOrderId(payOrderResponse.getOrderId());
                    payment.setStatus("PAID");
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                }
            }
            return payOrderResponse;
        } else {
            throw new RuntimeException("Failed to pay order: " + response.getStatusCode());
        }
    }

    public Object preauthorize(PreauthorizeRequest request) {
        String url = baseUrl + "/orders/" + request.getOrderId() + "/preauthorize";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-version", apiVersion);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<PreauthorizeRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            // Optionally persist preauthorization status
            // (Assume response contains orderId/paymentId/status)
            // Map<String, Object> respBody = (Map<String, Object>) response.getBody();
            // Persist logic here if needed
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to preauthorize payment: " + response.getStatusCode());
        }
    }

    public Object getOrder(String orderId) {
        String url = baseUrl + "/orders/" + orderId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", apiVersion);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            // Optionally persist order details
            // Map<String, Object> respBody = (Map<String, Object>) response.getBody();
            // Persist logic here if needed
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get order: " + response.getStatusCode());
        }
    }

    public Object createOrderSession(OrderSessionRequest request) {
        String url = baseUrl + "/orders";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-version", apiVersion);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<OrderSessionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();
            // Persist order session data if available
            if (responseBody instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> respMap = (Map<String, Object>) responseBody;
                if (respMap.containsKey("order_id")) {
                    Order order = new Order();
                    order.setOrderId((String) respMap.get("order_id"));
                    order.setStatus("SESSION_CREATED");
                    order.setCreatedAt(LocalDateTime.now());
                    orderRepository.save(order);
                }
            }
            return responseBody;
        } else {
            throw new RuntimeException("Failed to create order session: " + response.getStatusCode());
        }
    }
}
