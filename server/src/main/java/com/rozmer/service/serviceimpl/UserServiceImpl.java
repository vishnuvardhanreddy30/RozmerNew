package com.rozmer.service.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.rozmer.service.entities.GuestUser;
import com.rozmer.service.entities.UserFollower;
import com.rozmer.service.repo.GuestUserRepository;
import com.rozmer.service.repo.UserFollowerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.rozmer.service.dataobject.Response;
import com.rozmer.service.dataobject.SuccessResponse;
import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.exception.ServiceException;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.request.LoginUserRequestObject;
import com.rozmer.service.request.UserCreateRequestObject;
import com.rozmer.service.response.LoginResponse;
import com.rozmer.service.response.UserResponse;
import com.rozmer.service.service.EmailService;
import com.rozmer.service.service.UserService;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GuestUserRepository guestUserRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmailService emailService;

    @Autowired
    private UserFollowerRepository userFollowerRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;
    @Value("${spring.mail.sendername}")
    private String senderName;

    @Override
    public Response<UserResponse> createUser(UserCreateRequestObject userCreateRequestObject, String siteURL) throws UnsupportedEncodingException, MessagingException {

        log.info("createUser method in UserServiceImpl start | userRequestObject", userCreateRequestObject);
        User userEntity = modelMapper.map(userCreateRequestObject, User.class);
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new ResourceNotFoundException("User not found with  ", "emailId", userEntity.getEmail());
        }
        String randomCode = RandomString.make(64);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        //String encodedPassword = userEntity.getPassword();
        userEntity.setVerificationCode(randomCode);
        userEntity.setEnabled(false);
        userEntity.setPassword(encodedPassword);
        User user = userRepository.save(userEntity);
        sendVerificationEmail(user, siteURL);
        //UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        log.info("createUser method in UserServiceImpl end | userRequestObject", userCreateRequestObject);
        return new SuccessResponse<>("User Created Successfully");
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content = "<img src=\"[[IMGPATH]]\" style=\"display: block; width: 100%\"/>"
                + "<p style=\"font-weight:600;font-weight:16px;\">Hello [[name]],</p>"
                + "<p style=\"font-weight:400;font-weight:16px;\">Thank you for registering on Rozmer!</p>"
                + "<p style=\"font-weight:400;font-weight:16px;\">Good quality news is neither sensational nor boring.<p>"
                + "<p style=\"font-weight:400;font-weight:16px;\">And truly good quality news can only come about through collaboration not through competition.</p>"
                + "<p style=\"font-weight:400;font-weight:16px;\">Please click on verify to know how!</p>"
                + "<div style=\"text-align:center;\">"
                + "<a href=\"[[URL]]\" target=\"_self\" style=\"\n" +
                "    font-style: normal;\n" +
                "    line-height: 1.15;\n" +
                "    text-decoration: none;\n" +
                "    border-style: solid;\n" +
                "    word-wrap: break-word;\n" +
                "    display: inline-block;\n" +
                "    background-color: #00ffe6;\n" +
                "    border-color: #00ffe6;\n" +
                "    border-radius: 4px;\n" +
                "    border-width: 0px;\n" +
                "    font-family: arial,helvetica,sans-serif;\n" +
                "    font-size: 16px;\n" +
                "    height: 18px;\n" +
                "    padding-bottom: 12px;\n" +
                "    padding-left: 5px;\n" +
                "    padding-right: 5px;\n" +
                "    padding-top: 12px;\n" +
                "    width: 290px;\n" +
                "    text-align: center;\n" +
                "    color: #000;\n" +
                "    font-weight: bold;\n" +
                "\">VERIFY</a></div><br>"
//				+ "<div align=\"center\" style=\"color: #727272; font-size: 10px\">If you wish to unsubscribe from our newsletter, click <a style=\"color: #01a4c6\" href=\"https://6fbj4.r.bh.d.sendibt3.com/mk/un/T9KRRDIGP0JCJ3T5EroxXWVrV1tB0NXQyVydCFaT4S6Xk0qfp1c0lJ6EJ-729z6j95s1pb08bV-erbkiXSp5okhblVjRBP1vjikycnf0MjPNAUFkmP6zEeSV_YeB2su1c5jfL0_uRncRkLK1Nidr0aQis9k6wddx67uAMhCx6A\" rel=\"noreferrer\" target=\"_blank\">here</a></div>"
                + "<br>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/?code=" + user.getVerificationCode();

        content = content.replace("[[IMGPATH]]", (siteURL + "/email_logo.png"));
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        javaMailSender.send(message);

        System.out.println("Email has been sent");
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("User not found with  ", "emailId", user.getEmail());
        }
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }

    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public SuccessResponse<String> loginUser(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user) || user.isEnabled() != true) {
            throw new ResourceNotFoundException("User not found with  ", "emailId", email);
        }
        String userpwdfromDB = user.getPassword();
        if (user != null) {
            //String encryptedPassword =  user.getPassword();
            //String decryptPassword = Utility.decrypt(encryptedPassword);
            boolean isPasswordMatch = passwordEncoder.matches(password, userpwdfromDB);
            if (!isPasswordMatch) {
                throw new ResourceNotFoundException("Password for this ", "", email + " is incorrect");
            }
        }
        user.setLoggedIn(true);
        userRepository.save(user);
        return new SuccessResponse<>("Password Correct");

    }

    @Override
    public LoginResponse userLogin(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("User not found with  ", "emailId", email);
        }

        if (user != null && user.isEnabled() == true) {
            //String encryptedPassword =  user.getPassword();
            //String decryptPassword = Utility.decrypt(encryptedPassword);
            String userpwdfromDB = user.getPassword();
            boolean isPasswordMatch = passwordEncoder.matches(password, userpwdfromDB);
            if (!isPasswordMatch) {
                throw new ResourceNotFoundException("Password for this ", "", email + " is incorrect");
            }
            user.setLoggedIn(true);
        } else {
            throw new ResourceNotFoundException("User not enable with ", "emailId", email);
        }
        userRepository.save(user);
        return this.modelMapper.map(user, LoginResponse.class);

    }

    @Override
    public SuccessResponse<String> logUserOut(String email, String role) {

        if ("guest".equals(role)) {
            GuestUser guestUser = guestUserRepository.findByGuestEmail(email);
            if (ObjectUtils.isEmpty(guestUser)) {
                throw new ResourceNotFoundException("User not found with  ", "emailId", email);
            }
            if (guestUser.isLoggedIn()) {
                guestUser.setLoggedIn(false);
                guestUserRepository.save(guestUser);
            }
            return new SuccessResponse<>("User Logged Out");

        } else {
            User user = userRepository.findByEmail(email);
            if (ObjectUtils.isEmpty(user)) {
                throw new ResourceNotFoundException("User not found with  ", "emailId", email);
            }
            if (user != null && user.isEnabled() == true) {
                if (user.isLoggedIn()) {
                    user.setLoggedIn(false);
                    userRepository.save(user);
                }
                return new SuccessResponse<>("User Logged Out");

            } else {
                throw new ServiceException("User Not Found:", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public SuccessResponse<String> loginUser(LoginUserRequestObject loginUserRequestObject) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String email = loginUserRequestObject.getEmail();
        String password = loginUserRequestObject.getPassword();
        User user = userRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("User not found with  ", "emailId", email);
        }

        if (user != null && user.isEnabled() == true) {
            //String encryptedPassword =  user.getPassword();
            //String decryptPassword = Utility.decrypt(encryptedPassword);
            String userpwdfromDB = user.getPassword();
            boolean isPasswordMatch = passwordEncoder.matches(password, userpwdfromDB);
            if (!isPasswordMatch) {
                throw new ResourceNotFoundException("Password for this ", "", email + " is incorrect");
            }
            user.setLoggedIn(true);
        }
        userRepository.save(user);
        return new SuccessResponse<>("User Logged in Successfully!!");
    }

    @Override
    public LoginResponse login(LoginUserRequestObject loginUserRequestObject) {
        String email = loginUserRequestObject.getEmail();
        String password = loginUserRequestObject.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if ("guest".equals(loginUserRequestObject.getRole())) {

            GuestUser guestUser = guestUserRepository.findByGuestEmail(loginUserRequestObject.getEmail());
            if (ObjectUtils.isEmpty(guestUser)) {
                log.info("createGuestUser method in UserServiceImpl start | LoginUserRequestObject", loginUserRequestObject);
                GuestUser userEntity = new GuestUser();
                if (ObjectUtils.isEmpty(userEntity)) {
                    throw new ResourceNotFoundException("User not found with  ", "emailId", loginUserRequestObject.getEmail());
                }
                BCryptPasswordEncoder paswordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = paswordEncoder.encode(loginUserRequestObject.getPassword());
                userEntity.setGuestEmail(email);
                userEntity.setPassword(encodedPassword);
                userEntity.setRole(loginUserRequestObject.getRole());
                userEntity.setLoggedIn(Boolean.TRUE);
                guestUserRepository.save(userEntity);
                guestUser = guestUserRepository.findByGuestEmail(loginUserRequestObject.getEmail());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setEmail(guestUser.getGuestEmail());
                loginResponse.setRole(guestUser.getRole());
                return loginResponse;
            }
            guestUser.setLoggedIn(Boolean.TRUE);
            guestUserRepository.save(guestUser);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setEmail(guestUser.getGuestEmail());
            loginResponse.setRole(guestUser.getRole());
            return loginResponse;

        } else {

            User user = userRepository.findByEmail(loginUserRequestObject.getEmail());
            if (ObjectUtils.isEmpty(user)) {
                throw new ResourceNotFoundException("User not found with  ", "emailId", email);
            }

            if (user != null && user.isEnabled() == true) {
                String userpwdfromDB = user.getPassword();
                boolean isPasswordMatch = passwordEncoder.matches(password, userpwdfromDB);
                if (!isPasswordMatch) {
                    throw new ResourceNotFoundException("Password for this ", "", email + " is incorrect");
                }
                user.setLoggedIn(true);
            } else {
                throw new ResourceNotFoundException("User not enable with ", "emailId", email);
            }
            userRepository.save(user);
            return this.modelMapper.map(user, LoginResponse.class);
        }
    }


    @Override
    public UserResponse updateUser(UserCreateRequestObject userCreateRequestObject, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found for  ", "user id", userId));

        if (userCreateRequestObject.getFirstName() != null) {
            user.setFirstName(userCreateRequestObject.getFirstName());
        }
        if (userCreateRequestObject.getLastName() != null) {
            user.setLastName(userCreateRequestObject.getLastName());
        }
        if (userCreateRequestObject.getMobileNo() != null) {
            user.setMobileNo(userCreateRequestObject.getMobileNo());
        }
        User updateUser = this.userRepository.save(user);
        return this.modelMapper.map(updateUser, UserResponse.class);
    }


    public ResponseEntity<SuccessResponse<String>> followUser(Long followerId, Long followingId) {
        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followingOptional = userRepository.findById(followingId);

        if (followerOptional.isPresent() && followingOptional.isPresent()) {
            User follower = followerOptional.get();
            User following = followingOptional.get();

            if (!isAlreadyFollowing(follower, following)) {
                UserFollower userFollower = new UserFollower();
                userFollower.setFollower(follower);
                userFollower.setFollowing(following);
                userFollowerRepository.save(userFollower);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new SuccessResponse<>(followerOptional.get().getEmail() + " Followed "
                                + followingOptional.get().getEmail() + " Successfully!!")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new SuccessResponse<>(followerOptional.get().getEmail()
                    + " isAlreadyFollowing " + followingOptional.get().getEmail()));
        } else {
            throw new ResourceNotFoundException("User not enable with ", "followerId", followerId);
        }
    }

    public ResponseEntity<SuccessResponse<String>> unfollowUser(Long followerId, Long followingId) {
        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followingOptional = userRepository.findById(followingId);

        if (followerOptional.isPresent() && followingOptional.isPresent()) {
            User follower = followerOptional.get();
            User following = followingOptional.get();

            userFollowerRepository.deleteByFollowerAndFollowing(follower, following);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SuccessResponse<>(followerOptional.get().getEmail() + " unfollowed "
                    + followingOptional.get().getEmail() + " Successfully!!"));

        } else {
            throw new ResourceNotFoundException("User not enable with ", "followerId", followerId);
        }
    }

    private boolean isAlreadyFollowing(User follower, User following) {
        List<UserFollower> userFollowers = userFollowerRepository.findByFollowerAndFollowing(follower, following);
        return !userFollowers.isEmpty();
    }


    public List<User> getFollowers(Long loginUserId) {
        Optional<User> loginUserOptional = userRepository.findById(loginUserId);
        if (loginUserOptional.isPresent()) {
            User loginUser = loginUserOptional.get();
            List<UserFollower> userFollowers = userFollowerRepository.findByFollower(loginUser);

            return userFollowers.stream()
                    .map(UserFollower::getFollower)
                    .collect(Collectors.toList());
        } else {
            // User not found
            return Collections.emptyList();
        }
    }

    public List<User> getFollowings(Long loginUserId) {
        Optional<User> loginUserOptional = userRepository.findById(loginUserId);
        if (loginUserOptional.isPresent()) {
            User loginUser = loginUserOptional.get();
            List<UserFollower> userFollowings = userFollowerRepository.findByFollowing(loginUser);

            return userFollowings.stream()
                    .map(UserFollower::getFollowing)
                    .collect(Collectors.toList());
        } else {
            // User not found
            return Collections.emptyList();
        }
    }

    public List<User> findAllUsers() {
        Iterable<User> users = userRepository.findAll();

        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);
        return userList;
    }

}
