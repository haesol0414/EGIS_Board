package com.example.board.service;

import com.example.board.dto.LoginDTO;

public interface AuthService {
    // 로그인
    String login(LoginDTO loginDTO);
}
