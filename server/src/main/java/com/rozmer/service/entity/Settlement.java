package com.rozmer.service.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "settlements")
@Getter
@Setter
public class Settlement {
    @Id
    private String settlementId;
    private String orderId;
    private String settlementUtr;
    private Double amount;
    private LocalDateTime settledAt;
}
