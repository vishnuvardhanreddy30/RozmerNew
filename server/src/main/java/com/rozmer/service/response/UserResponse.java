package com.rozmer.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    
    private Long userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String verificationCode;
    private boolean enabled;


    
}
