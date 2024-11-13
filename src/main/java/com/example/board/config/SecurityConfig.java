package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // SecurityFilterChain 빈을 정의하여 보안 설정을 구성
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 요청에 대한 권한 설정
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        // 모든 요청을 허용 (보통은 로그인이나 특정 페이지에 대한 인증을 설정)
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 모든 경로에 대한 요청을 허용
                // CSRF 보호 설정 비활성화
                .csrf((csrf) -> csrf
                        .disable())
                // HTTP 헤더 설정
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

        ;
        return http.build();  // 설정된 보안 필터 체인을 빌드하여 반환
    }

    // 비밀번호 인코더를 설정하는 빈 정의
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}