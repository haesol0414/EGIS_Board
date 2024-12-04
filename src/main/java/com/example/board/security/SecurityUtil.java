package com.example.board.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SecurityUtil {
    public boolean isAdmin() {
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public String getLoggedInUserId() {
        return getAuthentication().getName();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }
        return authentication;
    }
}
