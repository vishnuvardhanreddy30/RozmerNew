package com.rozmer.service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article_access", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "accessed_at")
    private LocalDateTime accessedAt = LocalDateTime.now();
}

