package com.example.board.security;

public class JwtConstant {
    public static final String GRANT_TYPE = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000;  // 30분
    public static final String AUTHORIZATION_HEADER = "Authorization";
}
