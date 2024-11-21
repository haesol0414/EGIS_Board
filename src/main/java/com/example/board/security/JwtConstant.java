package com.example.board.security;

public class JwtConstant {
    // Bearer 토큰 앞부분
    public static final String GRANT_TYPE = "Bearer ";

    // JWT 관련 상수들
    public static final String SECRET_KEY = "823e399822c5170927c9802b3feb60b1fe54debefb406ca5f4eaf05e0014ea63";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000;  // 30분

    // 헤더에 포함된 Authorization 키 이름
    public static final String AUTHORIZATION_HEADER = "Authorization";
}
