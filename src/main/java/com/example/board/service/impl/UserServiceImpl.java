package com.example.board.service.impl;

import com.example.board.dto.CreateUserDTO;
import com.example.board.mapper.UserMapper;
import com.example.board.model.User;
import com.example.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 가입
    @Override
    public int signUp(CreateUserDTO createUserDTO) {
        User user = createUserDTO.toEntity();

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
        // 존재하지 않으면(false) -> true로 변환 -> Controller : true면 200
        return !userMapper.existsByUserId(userId);
    }
}
