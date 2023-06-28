package com.rozmer.service.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import com.rozmer.service.dataobject.Response;
import com.rozmer.service.dataobject.SuccessResponse;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.request.LoginUserRequestObject;
import com.rozmer.service.request.UserCreateRequestObject;
import com.rozmer.service.response.LoginResponse;
import com.rozmer.service.response.UserResponse;
import com.rozmer.service.service.UserService;

@RestController
@RequestMapping(value = "/add-user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    
    private String url;

    @Value("${ui.endpoint.url}") private String uiURL;

    @PostMapping()
    @CrossOrigin
    @ConfigurationProperties(prefix="endpoint")
    public ResponseEntity<Response<UserResponse>> createUser(
            @RequestBody UserCreateRequestObject userCreateRequestObject , HttpServletRequest request) throws ServiceException, UnsupportedEncodingException, MessagingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateRequestObject , uiURL));

    }

    // update post
	@PutMapping("/update-user/{userId}")
	@CrossOrigin
	public ResponseEntity<UserResponse> updatePost(@RequestBody UserCreateRequestObject userCreateRequestObject, @PathVariable Long userId) {

		UserResponse updateUser = this.userService.updateUser(userCreateRequestObject, userId);
		return new ResponseEntity<UserResponse>(updateUser, HttpStatus.OK);
	}

    @GetMapping("/verify")
    @CrossOrigin
	public ResponseEntity<Boolean> updateUser(@Param("code") String code) {
        boolean updatedUser = this.userService.verify(code);
		return ResponseEntity.ok(updatedUser);
	}

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    // @PostMapping("/user-login")
    // @CrossOrigin
    // public ResponseEntity<SuccessResponse<String>> loginUser(
    //         @Param("email") String email, @Param("password") String password)
    //         throws ServiceException, UnsupportedEncodingException, MessagingException {
    //     return new ResponseEntity<>(userService.loginUser(email, password), HttpStatus.CREATED);

    // }

    @PostMapping("/user-login")
    @CrossOrigin
    public ResponseEntity<SuccessResponse<String>> loginbyBody(
            @RequestBody LoginUserRequestObject loginUserRequestObject)
            throws ServiceException, UnsupportedEncodingException, MessagingException {

        //return ResponseEntity.status(HttpStatus.CREATED).body(userService.loginUser(email, password));
        return new ResponseEntity<>(userService.loginUser(loginUserRequestObject), HttpStatus.CREATED);

    }

    // @PostMapping("/login-user")
    // @CrossOrigin
	// public ResponseEntity<LoginResponse> userLogin(
    //     @Param("email") String email, @Param("password") String password) {
    //     LoginResponse userDto = this.userService.userLogin(email, password);
    //     return new ResponseEntity<LoginResponse>(userDto, HttpStatus.OK);
	// }

    //Login-user changes with RequestBody
    @PostMapping("/login-user")
    @CrossOrigin
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginUserRequestObject loginUserRequestObject)
            throws ServiceException, UnsupportedEncodingException, MessagingException {

                return new ResponseEntity<>(userService.login(loginUserRequestObject), HttpStatus.CREATED);

    }



    @PostMapping("/users/logout")
    @CrossOrigin
    public ResponseEntity<SuccessResponse<String>> logUserOut(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.logUserOut(email));
    }

}
