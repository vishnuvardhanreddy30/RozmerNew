package com.rozmer.service.repository;

import com.rozmer.service.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, String> {
}

