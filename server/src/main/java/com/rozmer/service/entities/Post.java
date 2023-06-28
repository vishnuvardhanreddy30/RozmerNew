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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;

	@Column(name = "post_title", length = 100, nullable = false)
	private String title;

	@Lob
	private String content;

	private String imageName;

	private String hidePost;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

	@ManyToOne
	@JsonIgnore
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)

	private Set<Comment> comments = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private Set<Question> questions = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private Set<Prating> prating = new HashSet<>();


}
