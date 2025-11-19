package com.rozmer.service.repo;

import com.rozmer.service.entities.User;
import com.rozmer.service.entities.UserCoinWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCoinWalletRepository extends JpaRepository<UserCoinWallet, Long> {

    Optional<UserCoinWallet> findByUser(User user);
}
