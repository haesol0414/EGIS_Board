package com.example.board.service;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.UserDetailDTO;

public interface AuthService {
    // 로그인
    UserDetailDTO login(LoginDTO loginDTO);
}
