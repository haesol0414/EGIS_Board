package com.example.board.service;

import com.example.board.dto.CreateUserDTO;

public interface UserService {
    // 회원 가입
    int signUp(CreateUserDTO createUserDTO);
    // 아이디 중복확인
    boolean checkUserId(String userId);
}
