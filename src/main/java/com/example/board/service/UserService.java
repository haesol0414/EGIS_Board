package com.example.board.service;

import com.example.board.dto.RequestCreateUserDTO;
import com.example.board.dto.RequestLoginDTO;

public interface UserService {
    // 회원 가입
    int signUp(RequestCreateUserDTO requestCreateUserDTO);
    // 아이디 중복확인
    boolean checkUserId(String userId);
    // 로그인
    void login(RequestLoginDTO requestLoginDTO);
}
