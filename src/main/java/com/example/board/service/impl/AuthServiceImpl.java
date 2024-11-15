package com.example.board.service.impl;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.UserDetailDTO;
import com.example.board.mapper.UserMapper;
import com.example.board.model.User;
import com.example.board.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetailDTO login(LoginDTO loginDTO) {
        User user = userMapper.findByUserId(loginDTO.getUserId());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return null;
        }

        return new UserDetailDTO(user.getUserId(), user.getUserName(), user.getRole().name());
    }
}
