package com.rozmer.service.service;

import com.rozmer.service.entities.EmailDetails;

// Interface
public interface EmailService {
 
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details , String siteURL);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
