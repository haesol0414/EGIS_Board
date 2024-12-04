package com.example.board.controller;

import com.example.board.dto.request.LoginDTO;
import com.example.board.dto.request.SignUpDTO;
import com.example.board.service.UserService;

import com.example.board.service.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserRestController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            // 서비스 호출로 JWT 및 사용자 정보 반환
            Map<String, Object> loginResponse = userService.login(loginDTO);

            // HttpOnly 쿠키 설정
            String token = (String) loginResponse.get("token");
            ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                    .httpOnly(true) // JavaScript에서 접근 불가
                    .secure(false) // HTTPS 환경에서는 true로 설정
                    .path("/") // 모든 경로에서 쿠키 전송
                    .maxAge(30 * 60)
                    .build();

            // 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of(
                            "isLoggedIn", true,
                            "userId", loginResponse.get("userId"),
                            "username", loginResponse.get("username"),
                            "role", loginResponse.get("role")
                    ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "msg", "아이디 또는 비밀번호가 잘못되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "msg", "로그인 처리 중 문제가 발생했습니다."
            ));
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
        try {
            String userId = userData.get("userId");
            boolean isAvailable = userService.checkUserId(userId);

            if (isAvailable) {
                return ResponseEntity.ok("사용 가능한 아이디 입니다.");
            } else {
                return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

//    // 회원 정보
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
//        // 쿠키에서 JWT 추출
//        String token = Arrays.stream(request.getCookies())
//                .filter(cookie -> "Authorization".equals(cookie.getName()))
//                .map(Cookie::getValue)
//                .findFirst()
//                .orElse(null);
//
//        // 비회원 상태 처리
//        if (token == null || !jwtProvider.validateToken(token)) {
//            return ResponseEntity.ok(Map.of(
//                    "isLoggedIn", false,
//                    "message", "비회원 상태입니다."
//            ));
//        }
//
//        // JWT에서 사용자 정보 추출
//        String username = jwtProvider.getUsernameFromToken(token);
//        String role = jwtProvider.getRoleFromToken(token);
//        String userId = jwtProvider.getUserIdFromToken(token);
//
//        // 회원 상태 처리
//        return ResponseEntity.ok(Map.of(
//                "isLoggedIn", true,
//                "userId", userId,
//                "username", username,
//                "role", role
//        ));
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 무효화
        ResponseCookie cookie = ResponseCookie.from("Authorization", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("로그아웃 성공");
    }
}
