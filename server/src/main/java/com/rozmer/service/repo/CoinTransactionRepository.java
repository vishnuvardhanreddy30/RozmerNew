package com.rozmer.service.repo;

import com.rozmer.service.entities.CoinTransaction;
import com.rozmer.service.entities.TransactionType;
import com.rozmer.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {

    List<CoinTransaction> findByUser(User user);
    List<CoinTransaction> findByUserAndTransactionType(User user, TransactionType transactionType);
}
