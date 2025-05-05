package com.judebalance.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.LoginRequest;
import com.judebalance.backend.request.PasswordResetConfirmRequest;
import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.LoginResponse;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.service.AuthService;
import com.judebalance.backend.service.EmailService;
import com.judebalance.backend.util.PasswordResetTokenStore;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordResetTokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;  // ✨ 추가 필요!

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> signup(@RequestBody RegisterRequest req) {
        try {
            RegisterResponse resp = authService.signup(req);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new RegisterResponse(null, null, e.getMessage())
            );
        }
    }

    /**
     * 비밀번호 재설정 API
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetConfirmRequest request) {
        String email = tokenStore.getEmailIfValid(request.getToken());
        if (email == null) {
            return ResponseEntity.badRequest().body("유효하지 않거나 만료된 토큰입니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenStore.remove(request.getToken()); // 토큰 재사용 방지

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }

    /**
     * 비밀번호 재설정 링크 전송 API
     */
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 사용자가 없습니다."));

        String token = tokenStore.createToken(email);
        emailService.sendResetPasswordEmail(email, token);

        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    }

    /**
     * 로그아웃 API (형식용)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 프론트에서 토큰 삭제 필요함 (백엔드는 상태 저장 안 함)
        return ResponseEntity.ok("로그아웃 처리됨 (프론트에서 토큰 삭제 필요)");
    }
}
