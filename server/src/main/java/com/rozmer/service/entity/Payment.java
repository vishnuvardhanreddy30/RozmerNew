package com.rozmer.service.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    private String paymentId;
    private String orderId;
    private String status;
    private double amount;
    private LocalDateTime paidAt;
}
