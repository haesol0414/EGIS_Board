package com.example.board.controller;

import com.example.board.dto.LoginDTO;
import com.example.board.service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String accessToken = authService.login(loginDTO);

        if (accessToken != null) {
            // 로그인 성공 시 accessToken 반환
            return ResponseEntity.ok(accessToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }
}
