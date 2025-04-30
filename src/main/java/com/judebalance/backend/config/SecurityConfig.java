// src/main/java/com/judebalance/backend/config/SecurityConfig.java
package com.judebalance.backend.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.judebalance.backend.util.JwtUtil;
import com.judebalance.backend.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    // 생성자 주입
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // PasswordEncoder 설정 (현재 개발용 NoOp)
    @Bean
    public PasswordEncoder passwordEncoder() {
       //   비밀번호 안전하게 해시 저장함
        return new BCryptPasswordEncoder();
    }

    // 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil);

        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/user/signup").permitAll()   // ✅ 여기가 핵심!
        .requestMatchers("/api/user/me").authenticated()
        .requestMatchers("/api/user/**").authenticated()
        .anyRequest().permitAll()
    )
    
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
