package com.rozmer.service.service;

import com.rozmer.service.dataobject.TransactionDto;

import java.util.List;

public interface WalletService {

    int getWalletBalance(Long userId);

    void rechargeCoins(Long userId, int coinsToAdd, String paymentRefId);

    List<TransactionDto> getUserTransactions(Long userId, String transactionType);

}
