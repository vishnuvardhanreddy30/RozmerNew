package com.rozmer.service.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pratings")
@Getter
@Setter
public class Prating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rating;

    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Post post;

    @DateTimeFormat(pattern = "DD-MM-YYYY")
    private Date addedDate;

    private String takeDown;
	private String comment;
}