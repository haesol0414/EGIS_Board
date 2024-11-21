package com.example.board.mapper;

import com.example.board.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 회원 가입
    @Insert("INSERT INTO users (user_id, user_name, password) VALUES (#{userId}, #{userName}, #{password})")
    @Options(keyProperty = "userId", useGeneratedKeys = false)
    int insertUser(UserVO user);

    // 아이디 중복 체크
    @Select("SELECT COUNT(*) > 0 FROM users WHERE user_id = #{userId}")
    boolean existsByUserId(String userId);

    // 회원 상세 조회
    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    UserVO findByUserId(String userId);
}
