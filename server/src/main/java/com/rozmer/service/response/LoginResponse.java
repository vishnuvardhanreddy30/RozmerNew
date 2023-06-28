package com.rozmer.service.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginResponse {

	private Long userId;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String mobileNo;

	private String username;

	private boolean loggedIn;
	
}
