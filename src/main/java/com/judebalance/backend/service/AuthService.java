package com.judebalance.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.LoginRequest;
import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.LoginResponse;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.util.AesEncryptUtil;
import com.judebalance.backend.util.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * 인증(Auth) 관련 비즈니스 로직 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구
    private final JwtUtil jwtUtil;                  // JWT 토큰 발급 유틸
    private final AesEncryptUtil aesEncryptUtil;    // AES 암호화 유틸

    /**
     * 로그인 처리
     */
    public LoginResponse login(LoginRequest request) {
        // 1) 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
}

        // 3) JWT 토큰 발급
        String token = jwtUtil.generateToken(
            user.getUsername(),
            List.of("ROLE_USER") // 권한 추가 가능
        );

        // 4) 로그인 성공 응답 반환
        return new LoginResponse(token, user.getUsername());
    }

    /**
     * 회원가입 처리
     */
    public RegisterResponse signup(RegisterRequest req) {
        // 1) 사용자명 중복 체크
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }

        // 2) 이메일 중복 체크
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 3) 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(req.getPassword());

        // 4) 이메일, 전화번호 AES 암호화
        String encryptedEmail = aesEncryptUtil.encrypt(req.getEmail());
        String encryptedPhone = aesEncryptUtil.encrypt(req.getPhoneNumber());

        // 5) User 객체 생성 및 저장
        User savedUser = userRepository.save(User.builder()
            .username(req.getUsername())
            .password(hashedPassword)
            .email(encryptedEmail)
            .phoneNumber(encryptedPhone)
            .nickname(req.getNickname())
           
            .gender(req.getGender())
            .age(req.getAge())
            .height(req.getHeight())
            .weight(req.getWeight())
            .fitnessLevel(req.getFitnessLevel())
            .isProfileSetupCompleted(false)
            .build()
        );

        // 6) 회원가입 성공 응답 반환
        return new RegisterResponse(
            savedUser.getUsername(),
            savedUser.getEmail(),
            "회원가입이 성공적으로 완료되었습니다."
        );
    }
}
