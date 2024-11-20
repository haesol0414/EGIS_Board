package com.example.board.service;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.SignUpDTO;

public interface UserService {
    // 로그인
    String login(LoginDTO loginDTO);
    // 회원 가입
    int signUp(SignUpDTO signUpDTO);
    // 아이디 중복확인
    boolean checkUserId(String userId);
}
