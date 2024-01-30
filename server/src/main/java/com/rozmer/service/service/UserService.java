package com.rozmer.service.service;


import com.rozmer.service.response.LoginResponse;
import com.rozmer.service.response.UserResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
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

    SuccessResponse<String> logUserOut (String email, String role);

    public boolean verify(String verificationCode);


    public void updateResetPasswordToken(String token, String email);

    public User getByResetPasswordToken(String token);

    public void updatePassword(User user, String newPassword);

    LoginResponse login (LoginUserRequestObject loginUserRequestObject);

    //update User
	UserResponse updateUser(UserCreateRequestObject userCreateRequestObject, Long userId);
    ResponseEntity<SuccessResponse<String>> followUser(Long followerId, Long followingId);
    ResponseEntity<SuccessResponse<String>> unfollowUser(Long followerId, Long followingId);

    List<User> getFollowers(Long loginUserId);
    List<User> getFollowings(Long loginUserId);
    List<User> findAllUsers();
}
