package com.rozmer.service.request;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequestObject {

    @Email(message = "Email address is not valid !!")
    private String email;
    private String password;
    
}
