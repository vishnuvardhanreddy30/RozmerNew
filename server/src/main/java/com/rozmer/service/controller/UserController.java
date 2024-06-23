package com.rozmer.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rozmer.service.dataobject.PostDto;
import com.rozmer.service.dataobject.User;
import com.rozmer.service.service.FileService;
import lombok.var;
import org.hibernate.engine.jdbc.StreamUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/add-user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private FileService fileService;

    private String url;

    @Value("${project.image.profile}")
    private String path;

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
    public ResponseEntity<SuccessResponse<String>> logUserOut(@RequestParam String email,@RequestParam String role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.logUserOut(email,role));
    }

    @GetMapping("/followUser/{followerId}/{followingId}")
    @CrossOrigin
    public ResponseEntity<SuccessResponse<String>> followUser(@PathVariable Long followerId,@PathVariable Long followingId) {

        return userService.followUser(followerId, followingId);
    }

    @GetMapping("/unfollowUser/{followerId}/{followingId}")
    @CrossOrigin
    public ResponseEntity<SuccessResponse<String>> unfollowUser(@PathVariable Long followerId,@PathVariable Long followingId) {

        return userService.unfollowUser(followerId, followingId);
    }

    @GetMapping("/followers/{loginUserId}")
    @CrossOrigin
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long loginUserId) {
        return ResponseEntity.ok(userService.getFollowers(loginUserId));
    }

    @GetMapping("/following/{loginUserId}")
    @CrossOrigin
    public ResponseEntity<List<com.rozmer.service.dataobject.User>> getFollowing(@PathVariable Long loginUserId) {
        return ResponseEntity.ok(userService.getFollowings(loginUserId));
    }

    @GetMapping("/getAllUsersWithFollowingFlag/{loginUserId}")
    @CrossOrigin
    public List<com.rozmer.service.dataobject.User> getAllUsersWithFollowingFlag(@PathVariable Long loginUserId) {
        return userService.getAllUsersWithFollowingFlag(loginUserId);
    }

    @PostMapping("/profile/image/upload/{loginUserId}")
    @CrossOrigin
    public ResponseEntity<UserResponse> uploadProfileImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable Long loginUserId) throws IOException {

        String fileName = this.fileService.uploadImage(path, image);
        UserResponse updateUser = this.userService.updateUser(UserCreateRequestObject.builder().imageName(fileName).build(), loginUserId);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);

    }

    // method to serve files
    @GetMapping(value = "/profile/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @CrossOrigin
    public void downloadProfileImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
