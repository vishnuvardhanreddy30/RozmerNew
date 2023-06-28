package com.rozmer.service.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rozmer.service.entities.User;
import com.rozmer.service.exception.ResourceNotFoundException;
import com.rozmer.service.repo.UserRepository;
import com.rozmer.service.service.UserService;

import net.bytebuddy.utility.RandomString;

@RestController
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;
     
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Value("${ui.endpoint.url}") private String uiURL;
    @Value("${spring.mail.username}") private String fromAddress;
    @Value("${spring.mail.sendername}") private String senderName;
 
    @PostMapping("/forgot_password")
    @CrossOrigin
    public String processForgotPassword(HttpServletRequest request, @Param("email") String email) {
                String token = RandomString.make(30);
        User user = userRepository.findByEmail(email);
		if(ObjectUtils.isEmpty(user) || user.isEnabled()!= true){
			throw new  ResourceNotFoundException("User not found with  ", "emailId", email);
		}
         
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = uiURL + "/?token=" + token;
            sendEmail(email, resetPasswordLink, user.getFullName(), uiURL);
             
        } catch (Exception ex) {
            
        } 
             
        return "mail send";
    }
     
    public void sendEmail(String recipientEmail, String link, String name, String uiURL)
        throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();              
    MimeMessageHelper helper = new MimeMessageHelper(message);
     
    helper.setFrom(fromAddress, senderName);
    helper.setTo(recipientEmail);
     
    String subject = "Here's the link to reset your password";
     
    String content = "<img src=\""+ (uiURL + "/email_logo.png") + "\" style=\"display: block; width: 100%\"/>"
            + "<p style=\"font-weight:600;font-weight:16px;\">Hello "+name+",</p>"
            + "<p style=\"font-weight:400;font-weight:16px;\">Did you know?</p>"
            + "<p style=\"font-weight:400;font-weight:16px;\">Between apps, subscriptions, banks, and email accounts, the average person has about 100 passwords.</p>"
            + "<p style=\"font-weight:400;font-weight:16px;\">4 out of 5 of us have forgotten at least 1 password in the last 90 days, and a quarter of us lose a password at least once a day. </p>"
            + "<p style=\"font-weight:400;font-weight:16px;\">Click the below link to reset your Rozmer password.  </p>"
            + "<div style=\"text-align:center\"><a href=\"" + link + "\" style=\"font-style: normal; font-weight: normal; line-height: 1.15; text-decoration: none; border-style: solid; word-wrap: break-word; display: inline-block; background-color: #0092ff; border-color: #0092ff; border-radius: 4px; border-width: 0px; color: #ffffff; font-family: arial,helvetica,sans-serif; font-size: 16px; height: 18px; padding-bottom: 12px; padding-left: 5px; padding-right: 5px; padding-top: 12px; width: 290px\">Reset password</a></p><br>"
//            + "<div align=\"center\" style=\"color: #727272; font-size: 10px\">If you wish to unsubscribe from our newsletter, click <a style=\"color: #01a4c6\" href=\"https://6fbj4.r.bh.d.sendibt3.com/mk/un/T9KRRDIGP0JCJ3T5EroxXWVrV1tB0NXQyVydCFaT4S6Xk0qfp1c0lJ6EJ-729z6j95s1pb08bV-erbkiXSp5okhblVjRBP1vjikycnf0MjPNAUFkmP6zEeSV_YeB2su1c5jfL0_uRncRkLK1Nidr0aQis9k6wddx67uAMhCx6A\" rel=\"noreferrer\" target=\"_blank\">here</a></div>"
            + "<br>"
            + "<p>Ignore this email if you do remember your password, "
            + "or you have not made the request.</p>";

    helper.setSubject(subject);
     
    helper.setText(content, true);
     
    mailSender.send(message);
} 
     
/*      
@PostMapping("/reset_password")
@CrossOrigin
public String processResetPassword( @Param("password") String password ,  @Param("email") String email) {

    User user = userRepository.findByEmail(email);
    String token = user.getResetPasswordToken();
    User userpwdtoken = userService.getByResetPasswordToken(token);
     
    if (userpwdtoken == null) {
        throw new  ResourceNotFoundException("Token has been Expired for   ", "emailId", email);
    } else {           
        userService.updatePassword(user, password);
         
    }
     
    return "Password Updated SuccessFully..";
} */

@PostMapping("/reset_password")
@CrossOrigin
public String resetPasswordByToken( @Param("password") String password ,  @Param("token") String token) {

    User user = userService.getByResetPasswordToken(token);
     
    if (user == null) {
        throw new  ResourceNotFoundException("Token has been Expired for", "", "" );
    } else {  
          try {
            userService.updatePassword(user, password);
          } catch (Exception e) {
            System.out.println(e.toString());
          }        
    }
     return "Password Reset Successfully!";
   // return ResponseEntity.status(HttpStatus.ACCEPTED);
}

}