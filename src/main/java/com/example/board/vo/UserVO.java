package com.example.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class UserVO {
    private String userId;
    private String userName;
    private String password;
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}