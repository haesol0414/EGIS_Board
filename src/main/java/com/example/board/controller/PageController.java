package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    // 메인 페이지
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

    @RequestMapping("/update")
    public String showUpdatePage() {
        return "boardUpdate";
    }
}