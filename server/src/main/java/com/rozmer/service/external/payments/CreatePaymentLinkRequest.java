package com.rozmer.service.external.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentLinkRequest {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private double amount;
    private String description;
}

