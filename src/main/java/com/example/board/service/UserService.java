package com.example.board.service;

import com.example.board.dto.request.LoginDTO;
import com.example.board.dto.request.SignUpDTO;

import java.util.Map;

public interface UserService {
    // 로그인
    Map<String, Object> login(LoginDTO loginDTO);
    // 회원 가입
    int signUp(SignUpDTO signUpDTO);
    // 아이디 중복확인
    boolean checkUserId(String userId);
}
