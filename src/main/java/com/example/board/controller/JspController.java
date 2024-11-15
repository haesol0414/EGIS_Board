package com.example.board.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JspController {
    @Value("${jwt.secret}")
    private String jwtSecret;

    // 홈 페이지
    @RequestMapping("/")
    public String showHomePage() {
        return "boardList";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
        System.out.println(jwtSecret);
        return "login";
    }

    @RequestMapping("/register")
    public String showSignUpPage() {
        return "register";
    }

    @RequestMapping("/write")
    public String showWritePage() {
        return "boardWrite";
    }

    @RequestMapping("/detail")
    public String showDetailPage() {
        return "boardDetail";
    }

    @RequestMapping("/update")
    public String showUpdatePage() {
        return "boardUpdate";
    }
}