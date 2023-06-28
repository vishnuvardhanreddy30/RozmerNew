package com.rozmer.service.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer questionId;

 @Column(name = "QUESTION", nullable = false)
 private String questions;

 @ManyToOne
 private User user;

 @ManyToOne
 private Post post;

 @DateTimeFormat(pattern = "DD-MM-YYYY")
 private Date addedDate;

 @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
 private Set<Answer> answer=new HashSet<>();


 @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
 private Set<Qrating> qrating = new HashSet<>();

}
