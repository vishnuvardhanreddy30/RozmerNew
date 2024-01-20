package com.rozmer.service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "guestuser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Email(message = "Email address is not valid !!")
	@Column(nullable = false, unique = true, length = 45)
	private String guestEmail;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "role", nullable = false, length = 20)
	private String role;

	@Column(name = "logged_In")
	private boolean loggedIn;
}
