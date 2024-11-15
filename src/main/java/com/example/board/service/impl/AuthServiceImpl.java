package com.example.board.service.impl;

import com.example.board.dto.LoginDTO;
import com.example.board.mapper.UserMapper;
import com.example.board.model.User;
import com.example.board.service.AuthService;
import com.example.board.service.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        User user = userMapper.findByUserId(loginDTO.getUserId());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return null;
        }

        // 로그인 성공 시 JWT 생성 후 반환
        return jwtProvider.generateToken(user.getUserId(), user.getUserName(), user.getRole().name());
    }
}
