// src/main/java/com/judebalance/backend/controller/UserController.java
package com.judebalance.backend.controller;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.response.UserProfileResponse;
import com.judebalance.backend.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인된 사용자의 프로필 조회 및 수정 컨트롤러
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화용

    /**
     * GET /api/user/me
     * - 현재 로그인한 사용자의 프로필 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 username 가져오기
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        UserProfileResponse profile = new UserProfileResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
        return ResponseEntity.ok(profile);
    }

    /**
     * PUT /api/user/me
     * - 현재 로그인한 사용자의 이메일, 비밀번호 수정
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 username 가져오기
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 이메일 수정
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            user.setEmail(updateRequest.getEmail());
        }

        // 비밀번호 수정 (암호화 후 저장)
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(user); // 수정사항 저장

        return ResponseEntity.ok("User updated successfully");
    }
}
