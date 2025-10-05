package com.rozmer.service.external.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cashfree")
public class CashfreeAuthController {

    @Autowired
    private CashfreeAuthService cashfreeAuthService;

    @PostMapping("/otp")
    public ResponseEntity<?> submitOrResendOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(cashfreeAuthService.submitOrResendOtp(request));
    }
}

