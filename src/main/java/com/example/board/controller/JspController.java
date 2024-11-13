package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JspController {
    // 홈 페이지
    @RequestMapping("/")
    public String showHomePage() {
        return "boardList";
    }

    @RequestMapping("/login")
    public String showLoginPage() {
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