package com.rozmer.service.dataobject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {

	private Long userId;

	@Email(message = "Email address is not valid !!")
	private String email;
	
	private String firstName;
	
	private String lastName;

	@NotEmpty
	private String about;

}
