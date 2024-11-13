package com.example.board.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private String userId;
    private String userName;
    private String password;
    private Role role;

    public enum Role {
        회원, 관리자
    }
}