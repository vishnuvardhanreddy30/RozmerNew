package com.rozmer.service.repo;

import com.rozmer.service.entities.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {

}
