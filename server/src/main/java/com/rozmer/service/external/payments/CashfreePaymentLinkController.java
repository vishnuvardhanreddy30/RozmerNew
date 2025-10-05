package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree/payment-links")
public class CashfreePaymentLinkController {

    @Autowired
    private CashfreePaymentLinkService cashfreePaymentLinkService;

    @PostMapping
    public ResponseEntity<?> createPaymentLink(@RequestBody CreatePaymentLinkRequest request) {
        return ResponseEntity.ok(cashfreePaymentLinkService.createPaymentLink(request));
    }

    @GetMapping("/{linkId}")
    public ResponseEntity<?> fetchPaymentLinkDetails(@PathVariable String linkId) {
        return ResponseEntity.ok(cashfreePaymentLinkService.fetchPaymentLinkDetails(linkId));
    }

    @GetMapping("/{linkId}/orders")
    public ResponseEntity<?> getOrdersForPaymentLink(@PathVariable String linkId) {
        return ResponseEntity.ok(cashfreePaymentLinkService.getOrdersForPaymentLink(linkId));
    }

    @PostMapping("/{linkId}/cancel")
    public ResponseEntity<?> cancelPaymentLink(@PathVariable String linkId) {
        return ResponseEntity.ok(cashfreePaymentLinkService.cancelPaymentLink(linkId));
    }
}

