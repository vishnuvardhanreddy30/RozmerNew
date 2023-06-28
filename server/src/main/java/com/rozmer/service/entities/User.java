package com.rozmer.service.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Email(message = "Email address is not valid !!")
	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;

	@Column(name = "username", nullable = false, length = 20)
	private String username;

	@Column(name = "last_name", nullable = false, length = 20)
	private String lastName;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	@Column(name = "mobile_number", nullable = false, length = 10)
	private String mobileNo;

	private boolean enabled;

	@Column(name = "logged_In")
	private boolean loggedIn;

	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Post> posts = new ArrayList<>();

	/* @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Ratings> ratings = new ArrayList<>(); */

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public User orElseThrow(Object object) {
		return null;
	}

}
