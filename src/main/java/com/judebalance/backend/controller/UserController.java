// src/main/java/com/judebalance/backend/controller/UserController.java
package com.judebalance.backend.controller;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.response.UserProfileResponse;
import com.judebalance.backend.request.UserUpdateRequest;
import com.judebalance.backend.request.UserProfileRequest;
import com.judebalance.backend.response.CommonResponse;
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
     * PUT /api/user/me
     * - 현재 로그인한 사용자의 이메일, 비밀번호 수정
     */
    @PutMapping("/me")
    public ResponseEntity<CommonResponse> updateUser(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        String username = authentication.getName();
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

        return ResponseEntity.ok(new CommonResponse("회원정보가 수정되었습니다."));
    }

    /**
     * POST /api/user/profile
     * - 현재 로그인한 사용자의 성별, 나이, 키, 몸무게, 운동 수준 저장
     */
    @PostMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody UserProfileRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setGender(request.getGender());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setFitnessLevel(request.getFitnessLevel());

        userRepository.save(user);

        return ResponseEntity.ok(new CommonResponse("프로필 정보가 저장되었습니다."));
    }

    @GetMapping("/me")
public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    return ResponseEntity.ok(
        new UserProfileResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getIsProfileSetupCompleted() // ✅ 추가
        )
    );
}

}
