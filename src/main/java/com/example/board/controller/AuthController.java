package com.example.board.controller;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.UserDetailDTO;
import com.example.board.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        UserDetailDTO user = authService.login(loginDTO);

        if (user != null) {
            HttpSession session = request.getSession(true);

            session.setAttribute("user", user);  // 유저 정보를 세션에 저장
            System.out.println(session.getAttribute("user"));  // 로그에 세션 정보 출력
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }
}
