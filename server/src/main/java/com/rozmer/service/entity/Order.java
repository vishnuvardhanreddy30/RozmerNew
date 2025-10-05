package com.rozmer.service.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "cf_order_id")
    private String cfOrderId;

    @Column(name = "payment_session_id")
    private String paymentSessionId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "order_note")
    private String orderNote;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "notify_url")
    private String notifyUrl;

    @Column(name = "payment_methods")
    private String paymentMethods;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "authorized_amount")
    private Double authorizedAmount;

    // Constructors
    public Order() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
