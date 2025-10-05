package com.rozmer.service.external.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSessionRequest {
    private String orderId;
    private String customerId;
}

