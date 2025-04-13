package com.rozmer.service.service;

public interface WalletService {

    int getWalletBalance(Long userId);

    void rechargeCoins(Long userId, int coinsToAdd, String paymentRefId);
}
