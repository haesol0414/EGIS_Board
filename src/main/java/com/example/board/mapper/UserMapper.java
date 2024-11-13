package com.example.board.mapper;

import com.example.board.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (user_id, user_name, password) VALUES (#{userId}, #{userName}, #{password})")
    @Options(keyProperty = "userId", useGeneratedKeys = false)
    int insertUser(User user);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE user_id = #{userId}")
    boolean existsByUserId(String userId);
}
