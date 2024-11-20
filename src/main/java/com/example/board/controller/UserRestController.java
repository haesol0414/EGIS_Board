package com.example.board.controller;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.SignUpDTO;
import com.example.board.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String accessToken = userService.login(loginDTO);

        if (accessToken != null) {
            // 로그인 성공 시 accessToken 반환
            return ResponseEntity.ok(accessToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            int result = userService.signUp(signUpDTO);
            if (result > 0) {
                return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("회원가입 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 예기치 않은 오류가 발생했습니다: " + e.getMessage());
        }
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