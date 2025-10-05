package com.rozmer.service.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_links")
@Getter
@Setter
public class PaymentLink {
    @Id
    private String linkId;
    private String customerEmail;
    private String customerPhone;
    private Double amount;
    private String status;
    private String linkUrl;
    private LocalDateTime createdAt;
}
