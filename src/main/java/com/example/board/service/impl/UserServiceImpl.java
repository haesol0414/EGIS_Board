package com.example.board.service.impl;

import com.example.board.dto.request.LoginDTO;
import com.example.board.dto.request.SignUpDTO;
import com.example.board.mapper.UserMapper;
import com.example.board.service.UserService;
import com.example.board.service.security.JwtProvider;
import com.example.board.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    // 로그인
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        UserVO user = userMapper.findByUserId(loginDTO.getUserId());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 권한 설정
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // JWT 토큰 생성
        Map<String, Object> tokenResponse = jwtProvider.generateToken(user.getUserId(), user.getUserName(), authorities);

        return Map.of(
                "token", tokenResponse.get("token"),
                "expiresAt", tokenResponse.get("expiresAt"),
                "userId", user.getUserId(),
                "username", user.getUserName(),
                "role", user.getRole().name()
        );
    }

    // 회원 가입
    @Override
    public int signUp(SignUpDTO signUpDTO) {
        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        // 암호화된 패스워드를 포함한 User 객체 생성
        UserVO encryptedUser = UserVO.builder()
                .userId(signUpDTO.getUserId())
                .userName(signUpDTO.getUserName())
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
