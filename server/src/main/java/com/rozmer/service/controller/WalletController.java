package com.rozmer.service.controller;

import com.rozmer.service.dataobject.CoinRechargeRequestDto;
import com.rozmer.service.dataobject.TransactionDto;
import com.rozmer.service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam Long userId) {
        int balance = walletService.getWalletBalance(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("walletBalance", balance);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recharge")
    @CrossOrigin
    public ResponseEntity<String> rechargeWallet(@RequestBody CoinRechargeRequestDto request) {
        walletService.rechargeCoins(request.getUserId(), request.getCoinsToAdd(), request.getPaymentReferenceId());
        return ResponseEntity.ok("Recharge successful");
    }

    @GetMapping("/transactions")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> getTransactions(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "all") String transactionType) {

        List<TransactionDto> transactions = walletService.getUserTransactions(userId, transactionType);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("transactions", transactions);

        return ResponseEntity.ok(response);
    }

}
