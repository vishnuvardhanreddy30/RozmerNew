package com.rozmer.service.external.payments;

import com.rozmer.service.entity.Settlement;
import com.rozmer.service.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CashfreeSettlementService {
    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${cashfree.api.url}")
    private String cashfreeApiUrl;
    @Value("${cashfree.app.id}")
    private String appId;
    @Value("${cashfree.secret.key}")
    private String secretKey;

    public Object getSettlementsByOrderId(String orderId) {
        String url = cashfreeApiUrl + "/orders/" + orderId + "/settlements";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBody = response.getBody();
            if (responseBody instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseBody;
                if (body.containsKey("settlements")) {
                    Object settlementsObj = body.get("settlements");
                    if (settlementsObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> settlements = (List<Map<String, Object>>) settlementsObj;
                        List<Settlement> saved = new ArrayList<>();
                        for (Map<String, Object> s : settlements) {
                            Settlement settlement = new Settlement();
                            settlement.setSettlementId((String) s.get("settlement_id"));
                            settlement.setOrderId(orderId);
                            settlement.setSettlementUtr((String) s.get("settlement_utr"));
                            settlement.setAmount(s.get("amount") != null ? Double.valueOf(s.get("amount").toString()) : null);
                            settlement.setSettledAt(LocalDateTime.now());
                            saved.add(settlementRepository.save(settlement));
                        }
                        return saved;
                    }
                }
            }
            return Collections.emptyList();
        } else {
            throw new RuntimeException("Failed to get settlements by order ID from Cashfree");
        }
    }

    public Object getAllSettlements(String settlementId, String settlementUtr, String fromDate, String toDate) {
        String url = cashfreeApiUrl + "/settlements";
        // Add query params if provided
        List<String> params = new ArrayList<>();
        if (settlementId != null) params.add("settlement_id=" + settlementId);
        if (settlementUtr != null) params.add("settlement_utr=" + settlementUtr);
        if (fromDate != null) params.add("from_date=" + fromDate);
        if (toDate != null) params.add("to_date=" + toDate);
        if (!params.isEmpty()) url += "?" + String.join("&", params);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", appId);
        headers.set("x-client-secret", secretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Object responseBody = response.getBody();
            if (responseBody instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseBody;
                if (body.containsKey("settlements")) {
                    Object settlementsObj = body.get("settlements");
                    if (settlementsObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> settlements = (List<Map<String, Object>>) settlementsObj;
                        List<Settlement> saved = new ArrayList<>();
                        for (Map<String, Object> s : settlements) {
                            Settlement settlement = new Settlement();
                            settlement.setSettlementId((String) s.get("settlement_id"));
                            settlement.setOrderId((String) s.get("order_id"));
                            settlement.setSettlementUtr((String) s.get("settlement_utr"));
                            settlement.setAmount(s.get("amount") != null ? Double.valueOf(s.get("amount").toString()) : null);
                            settlement.setSettledAt(LocalDateTime.now());
                            saved.add(settlementRepository.save(settlement));
                        }
                        return saved;
                    }
                }
            }
            return Collections.emptyList();
        } else {
            throw new RuntimeException("Failed to get all settlements from Cashfree");
        }
    }
}
