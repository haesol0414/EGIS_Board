package com.example.board.service.impl;

import com.example.board.dto.LoginDTO;
import com.example.board.dto.SignUpDTO;
import com.example.board.mapper.UserMapper;
import com.example.board.model.User;
import com.example.board.service.UserService;
import com.example.board.service.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
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

    // 회원 가입
    @Override
    public int signUp(SignUpDTO signUpDTO) {
        User user = signUpDTO.toEntity();

        //패스워드 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // 암호화된 패스워드를 포함한 User 객체 생성
        User encryptedUser = User.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .password(encodedPassword)
                .build();

        return userMapper.insertUser(encryptedUser);
    }

    // 아이디 중복체크
    @Override
    public boolean checkUserId(String userId) {
        // 아이디가 존재하는지 확인하고, 존재하지 않을 경우 사용 가능
        return !userMapper.existsByUserId(userId);
    }
}
