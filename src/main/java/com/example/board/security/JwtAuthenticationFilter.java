package com.example.board.security;

import com.example.board.service.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 쿠키에서 토큰 추출
        String token = resolveTokenFromCookies(httpRequest);
        log.debug("Extracted Token from Cookie: {}", token);

        // 토큰 유효성 검사 및 인증 설정
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            log.debug("Authentication successful for user: {}", authentication.getName());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.debug("No valid token found in cookies.");
        }

        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    /**
     * 쿠키에서 JWT 토큰 추출
     */
    private String resolveTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "Authorization".equals(cookie.getName())) // "Authorization" 쿠키 이름 필터링
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}