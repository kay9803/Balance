package com.judebalance.backend.service;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.LoginRequest;
import com.judebalance.backend.response.LoginResponse;
import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 인증(Auth) 관련 비즈니스 로직 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final JwtUtil jwtUtil;                  // JWT 유틸

    /**
     * 1) 로그인 처리
     */
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 발급
        String token = jwtUtil.generateToken(
            user.getUsername(),
            List.of("ROLE_USER")
        );

        // 발급된 토큰과 username 반환
        return new LoginResponse(token, user.getUsername());
    }

    /**
     * 2) 회원가입 처리
     */
    public RegisterResponse signup(RegisterRequest req) {
        // 중복체크
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        // 비밀번호 암호화
        String hashed = passwordEncoder.encode(req.getPassword());

        // 사용자 저장 (✨ gender 필드 추가)
        User saved = userRepository.save(User.builder()
            .username(req.getUsername())
            .password(hashed)
            .email(req.getEmail())
            .gender(req.getGender()) // ← 추가된 부분!
            .build()
        );

        // 응답 DTO 반환
        return new RegisterResponse(saved.getUsername(), saved.getEmail(), "가입 성공");
    }
}
