package com.example.board.controller;

import com.example.board.dto.RequestCreateUserDTO;
import com.example.board.dto.RequestLoginDTO;
import com.example.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/users/signup")
    public int singUp(@RequestBody RequestCreateUserDTO requestCreateUserDTO) {
        return userService.signUp(requestCreateUserDTO);
    }

    // 아이디 중복체크
    @PostMapping("/check-id")
    @ResponseBody
    public ResponseEntity<String> checkUserId(@RequestBody Map<String, String> userData) {
        String userId = userData.get("userId"); // userId 추출
        boolean isAvailable = userService.checkUserId(userId);

        if (isAvailable) {
            return ResponseEntity.ok("사용 가능한 아이디");
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
    }


    // 로그인
    @PostMapping("/users/login")
    public void login(@RequestBody RequestLoginDTO requestLoginDTO) {
        return userService.login(requestLoginDTO);
    }
}
