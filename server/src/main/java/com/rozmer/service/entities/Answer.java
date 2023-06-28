package com.rozmer.service.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answer")
@Getter
@Setter
public class Answer {

@Id
@Column(name = "ANSWER_ID", nullable = false)
@GeneratedValue
private Integer answerId;

 @Column(name = "ANSWER", nullable = false)
 private String answer;

 @ManyToOne
 private User user;

 @ManyToOne
 private Question question;

 @DateTimeFormat(pattern = "DD-MM-YYYY")
 private Date addedDate;
}
