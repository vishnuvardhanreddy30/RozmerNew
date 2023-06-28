package com.rozmer.service.service;


import com.rozmer.service.response.LoginResponse;
import com.rozmer.service.response.UserResponse;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.rozmer.service.dataobject.Response;
import com.rozmer.service.dataobject.SuccessResponse;
import com.rozmer.service.entities.User;
import com.rozmer.service.request.LoginUserRequestObject;
import com.rozmer.service.request.UserCreateRequestObject;

@Service
public interface UserService {

    Response<UserResponse> createUser (UserCreateRequestObject userCreateRequestObject , String siteURL) throws UnsupportedEncodingException, MessagingException;

    SuccessResponse<String> loginUser (String email,  String password);

    SuccessResponse<String> loginUser (LoginUserRequestObject loginUserRequestObject);

    LoginResponse userLogin (String email,  String password);

    SuccessResponse<String> logUserOut (String email);

    public boolean verify(String verificationCode);


    public void updateResetPasswordToken(String token, String email);

    public User getByResetPasswordToken(String token);

    public void updatePassword(User user, String newPassword);

    LoginResponse login (LoginUserRequestObject loginUserRequestObject);

    //update User
	UserResponse updateUser(UserCreateRequestObject userCreateRequestObject, Long userId);


    
}
