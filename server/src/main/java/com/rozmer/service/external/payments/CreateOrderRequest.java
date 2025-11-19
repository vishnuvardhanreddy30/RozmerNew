package com.rozmer.service.external.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private String orderId;
    private String orderAmount;
    private String customerPhone;
    private String customerEmail;
    private String returnUrl;
}
