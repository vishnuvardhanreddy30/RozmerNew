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
@Table(name = "qratings")
@Getter
@Setter
public class Qrating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rating;

    @ManyToOne
    private User user;

    @DateTimeFormat(pattern = "DD-MM-YYYY")
    private Date addedDate;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

}