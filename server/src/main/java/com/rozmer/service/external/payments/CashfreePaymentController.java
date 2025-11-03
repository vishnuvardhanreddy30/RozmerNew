package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cashfree")
@CrossOrigin
public class CashfreePaymentController {

    @Autowired
    private CashfreePaymentService cashfreePaymentService;

    @PostMapping("/payment-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        Map<String, Object> response = cashfreePaymentService.createOrder(
                request.getOrderId(),
                request.getOrderAmount(),
                request.getCustomerPhone(),
                request.getCustomerEmail(),
                request.getReturnUrl()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}/payments")
    public ResponseEntity<?> getPaymentsForOrder(@PathVariable String orderId) {
        Map<String, Object> response = cashfreePaymentService.getPaymentsForOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable String paymentId) {
        return ResponseEntity.ok(cashfreePaymentService.getPaymentById(paymentId));
    }
}
