package com.rozmer.service.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String content;

	@ManyToOne
	private Post post;

	@ManyToOne
	private User user;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private Set<Crating> crating = new HashSet<>();

}
