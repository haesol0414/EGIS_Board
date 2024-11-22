package com.example.board.mapper;

import com.example.board.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 회원 가입
    int insertUser(UserVO user);

    // 아이디 중복 체크
    boolean existsByUserId(String userId);

    // 회원 상세 조회
    UserVO findByUserId(String userId);
}
