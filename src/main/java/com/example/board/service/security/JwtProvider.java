package com.example.board.service.security;

import com.example.board.security.JwtConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {
    private final Key key;

    // 키 생성
    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // AccessToken 생성 메서드
    public String generateToken(String userId, String userName, String role) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + JwtConstant.ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 사용자 ID
                .setExpiration(accessTokenExpiresIn) // 만료 시간 설정
                .claim("userName", userName) // 사용자 이름 추가
                .claim("role", role) // 권한 추가
                .signWith(this.key) // 서명
                .compact();
    }

    // 인증 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 만료 여부 확인
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 확인
            Date expiration = claims.getExpiration();
            System.out.println(expiration);
            if (expiration.before(new Date())) { // 현재 시간보다 만료 시간이 이전이면 만료
                log.info("JWT Token 만료");
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 클레임 문자열이 비어 있습니다.", e);
        }
        
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
