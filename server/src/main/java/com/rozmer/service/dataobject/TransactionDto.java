package com.rozmer.service.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private Long transactionId;
    private Long userId;
    private String type; // "credit" or "debit"
    private Integer coins;
    private Long postId; // only for unlocks
    private String description; // payment reference or "Unlocked post #id"
    private LocalDateTime date;
}
