package com.example.board.dto;

import com.example.board.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateUserDTO {
    private String userId;
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userName(userName)
                .password(password)
                .build();
    }
}
