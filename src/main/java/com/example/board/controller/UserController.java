package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String showSignUpPage() {
        return "register";
    }
}

