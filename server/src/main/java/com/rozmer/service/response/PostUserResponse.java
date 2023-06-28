package com.rozmer.service.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostUserResponse {

	private String firstName;
	private String lastName;
	private Long userId;

}
