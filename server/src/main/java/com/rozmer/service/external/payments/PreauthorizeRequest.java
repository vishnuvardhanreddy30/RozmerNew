package com.rozmer.service.external.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreauthorizeRequest {
    private String orderId;
    private String paymentId;
    private String action; // capture or void
}

