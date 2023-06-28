package com.rozmer.service.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "crating")
@Getter
@Setter
public class Crating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rating;

    @ManyToOne
    private User user;

    @DateTimeFormat(pattern = "DD-MM-YYYY")
    private Date addedDate;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}