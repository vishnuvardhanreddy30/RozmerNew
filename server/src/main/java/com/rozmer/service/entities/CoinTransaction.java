package com.rozmer.service.entities;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "coin_transactions")
@Getter
@Setter
@NoArgsConstructor
public class CoinTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Optional: link to the post (nullable for top-ups, etc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;  // CREDIT or DEBIT

    @Column(nullable = false)
    private int amount;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime = LocalDateTime.now();

    @Column
    private String description;

    public CoinTransaction(User user, Post post, TransactionType type, int amount, String description) {
        this.user = user;
        this.post = post;
        this.transactionType = type;
        this.amount = amount;
        this.description = description;
        this.transactionTime = LocalDateTime.now();
    }
}


