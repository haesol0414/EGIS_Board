package com.example.board.controller;

import com.example.board.dto.CreateUserDTO;
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
    public int singUp(@RequestBody CreateUserDTO createUserDTO) {
        return userService.signUp(createUserDTO);
    }

    // 아이디 중복체크
    @PostMapping("/check-id")
    @ResponseBody
    public ResponseEntity<String> checkUserId(@RequestBody Map<String, String> userData) {
        String userId = userData.get("userId");
        boolean isAvailable = userService.checkUserId(userId);

        if (isAvailable) {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
    }
}
