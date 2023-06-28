package com.rozmer.service.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestObject {

    @Email(message = "Email address is not valid !!")
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Size(max = 10 , message = "Mobile Number is Not valid !!")
    private String mobileNo;
    private boolean loggedIn;
    private String username;

    
}
