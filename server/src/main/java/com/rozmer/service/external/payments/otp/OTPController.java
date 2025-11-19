package com.rozmer.service.external.payments.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/authenticate/{cf_payment_id}")
    public ResponseEntity<String> authenticateOTP(
            @PathVariable("cf_payment_id") String cfPaymentId,
            @RequestBody OTPRequest otpRequest) {
        return otpService.authenticateOTP(cfPaymentId, otpRequest);
    }
}

