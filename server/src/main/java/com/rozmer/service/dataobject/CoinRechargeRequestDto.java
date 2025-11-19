package com.rozmer.service.dataobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinRechargeRequestDto {
    private Long userId;
    private int coinsToAdd;
    private String paymentReferenceId; // optional, for tracking
}

