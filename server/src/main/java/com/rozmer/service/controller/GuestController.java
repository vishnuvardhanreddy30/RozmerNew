package com.rozmer.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "redirect:/api/posts"; // Return the name of the guest login form view
    }

    @PostMapping("/login")
    public String processLogin() {
        // Handle guest login logic here
        return "redirect:/guest/home"; // Redirect to the guest home page
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "guest-home"; // Return the name of the guest home page view
    }
}

