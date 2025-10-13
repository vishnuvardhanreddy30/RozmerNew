package com.rozmer.service.external.payments;

import com.rozmer.service.entity.PaymentLink;
import com.rozmer.service.repository.PaymentLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CashfreePaymentLinkService {
    @Autowired
    private PaymentLinkRepository paymentLinkRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${cashfree.api.url}")
    private String cashfreeApiUrl;
    @Value("${cashfree.app.id}")
    private String appId;
    @Value("${cashfree.key}")
    private String secretKey;

    public Object createPaymentLink(CreatePaymentLinkRequest request) {
        String url = cashfreeApiUrl + "/payment-links";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<CreatePaymentLinkRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
        Object responseBody = response.getBody();
        if (response.getStatusCode().is2xxSuccessful() && responseBody instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) responseBody;
            PaymentLink link = new PaymentLink();
            link.setLinkId((String) body.get("link_id"));
            link.setCustomerEmail(request.getCustomerEmail());
            link.setCustomerPhone(request.getCustomerPhone());
            link.setAmount(request.getAmount());
            link.setStatus((String) body.get("status"));
            link.setLinkUrl((String) body.get("link_url"));
            link.setCreatedAt(LocalDateTime.now());
            paymentLinkRepository.save(link);
            return link;
        }
        throw new RuntimeException("Failed to create payment link with Cashfree");
    }

    public Object fetchPaymentLinkDetails(String linkId) {
        String url = cashfreeApiUrl + "/payment-links/" + linkId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        Object responseBody = response.getBody();
        if (response.getStatusCode().is2xxSuccessful() && responseBody instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) responseBody;
            PaymentLink link = new PaymentLink();
            link.setLinkId((String) body.get("link_id"));
            link.setCustomerEmail((String) body.get("customer_email"));
            link.setCustomerPhone((String) body.get("customer_phone"));
            link.setAmount(body.get("amount") != null ? Double.valueOf(body.get("amount").toString()) : null);
            link.setStatus((String) body.get("status"));
            link.setLinkUrl((String) body.get("link_url"));
            link.setCreatedAt(LocalDateTime.now());
            paymentLinkRepository.save(link);
            return link;
        }
        throw new RuntimeException("Failed to fetch payment link details from Cashfree");
    }

    public Object getOrdersForPaymentLink(String linkId) {
        String url = cashfreeApiUrl + "/payment-links/" + linkId + "/orders";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to get orders for payment link from Cashfree");
    }

    public Object cancelPaymentLink(String linkId) {
        String url = cashfreeApiUrl + "/payment-links/" + linkId + "/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
        Object responseBody = response.getBody();
        if (response.getStatusCode().is2xxSuccessful() && responseBody instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) responseBody;

            // Try to update existing payment link, or create new one if not found
            PaymentLink link = paymentLinkRepository.findById(linkId).orElse(new PaymentLink());
            link.setLinkId(linkId);
            link.setStatus((String) body.get("status"));
            if (link.getCreatedAt() == null) {
                link.setCreatedAt(LocalDateTime.now());
            }
            paymentLinkRepository.save(link);
            return link;
        }
        throw new RuntimeException("Failed to cancel payment link with Cashfree");
    }
    // ...existing code for other methods...

}
