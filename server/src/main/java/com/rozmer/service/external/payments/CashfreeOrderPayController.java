package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree")
public class CashfreeOrderPayController {

    @Autowired
    private CashfreeOrderPayService cashfreeOrderPayService;

    @PostMapping("/pay-order")
    public ResponseEntity<PayOrderResponse> payOrder(@RequestBody PayOrderRequest request) {
        PayOrderResponse response = cashfreeOrderPayService.payOrder(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/preauthorize")
    public ResponseEntity<?> preauthorize(@RequestBody PreauthorizeRequest request) {
        return ResponseEntity.ok(cashfreeOrderPayService.preauthorize(request));
    }

    @GetMapping("/orders/pay/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(cashfreeOrderPayService.getOrder(orderId));
    }

    @PostMapping("/orders/sessions")
    public ResponseEntity<?> createOrderSession(@RequestBody OrderSessionRequest request) {
        return ResponseEntity.ok(cashfreeOrderPayService.createOrderSession(request));
    }
}
