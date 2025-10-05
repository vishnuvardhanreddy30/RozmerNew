package com.rozmer.service.repository;

import com.rozmer.service.entity.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLinkRepository extends JpaRepository<PaymentLink, String> {
}
