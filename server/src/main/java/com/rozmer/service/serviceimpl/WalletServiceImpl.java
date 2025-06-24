package com.rozmer.service.serviceimpl;

import com.rozmer.service.dataobject.TransactionDto;
import com.rozmer.service.entities.CoinTransaction;
import com.rozmer.service.entities.TransactionType;
import com.rozmer.service.entities.User;
import com.rozmer.service.entities.UserCoinWallet;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.CoinTransactionRepository;
import com.rozmer.service.repo.UserCoinWalletRepository;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCoinWalletRepository walletRepository;

    @Autowired
    private CoinTransactionRepository coinTransactionRepository;

    @Override
    public int getWalletBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return walletRepository.findByUser(user)
                .map(UserCoinWallet::getTotalCoins)
                .orElse(0);
    }

    @Override
    public void rechargeCoins(Long userId, int coinsToAdd, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update wallet balance
        UserCoinWallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    UserCoinWallet w = new UserCoinWallet();
                    w.setUser(user);
                    w.setTotalCoins(0);
                    return w;
                });
        wallet.setTotalCoins(wallet.getTotalCoins() + coinsToAdd);
        walletRepository.save(wallet);

        // Log the coin transaction
        CoinTransaction tx = new CoinTransaction(
                user,
                null,  // No post involved for recharges
                TransactionType.CREDIT,
                coinsToAdd,
                description
        );
        coinTransactionRepository.save(tx);
    }

    @Override
    public List<TransactionDto> getUserTransactions(Long userId, String transactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<CoinTransaction> transactions;

        switch (transactionType.toLowerCase()) {
            case "recharge":
                transactions = coinTransactionRepository.findByUserAndTransactionType(user, TransactionType.CREDIT);
                break;
            case "unlock":
                transactions = coinTransactionRepository.findByUserAndTransactionType(user, TransactionType.DEBIT);
                break;
            default:
                transactions = coinTransactionRepository.findByUser(user);
                break;
        }

        return transactions.stream().map(tx -> TransactionDto.builder()
                .transactionId(tx.getId())
                .userId(userId)
                .type(tx.getTransactionType().name().toLowerCase())
                .coins(tx.getAmount())
                .postId(Long.valueOf(tx.getPost() != null ? tx.getPost().getPostId() : 0))
                .description(tx.getDescription())
                .date(tx.getTransactionTime())
                .build()).toList();
    }

}

