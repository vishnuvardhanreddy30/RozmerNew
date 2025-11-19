package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree")
@CrossOrigin
public class CashfreeSettlementController {

    @Autowired
    private CashfreeSettlementService cashfreeSettlementService;

    @GetMapping("/orders/{orderId}/settlements")
    public ResponseEntity<?> getSettlementsByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(cashfreeSettlementService.getSettlementsByOrderId(orderId));
    }

    @GetMapping("/settlements")
    public ResponseEntity<?> getAllSettlements(@RequestParam(required = false) String settlementId,
                                               @RequestParam(required = false) String settlementUtr,
                                               @RequestParam(required = false) String fromDate,
                                               @RequestParam(required = false) String toDate) {
        return ResponseEntity.ok(cashfreeSettlementService.getAllSettlements(settlementId, settlementUtr, fromDate, toDate));
    }
}

