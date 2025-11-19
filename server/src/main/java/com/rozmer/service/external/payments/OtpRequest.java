package com.rozmer.service.external.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {
    private String phone;
    private String otp;
    private boolean resend;
}
