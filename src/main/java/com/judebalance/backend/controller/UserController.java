// src/main/java/com/judebalance/backend/controller/UserController.java
package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.ChangePasswordRequest;
import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.request.UserUpdateRequest;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.response.UserProfileResponse;
import com.judebalance.backend.response.UserSearchResponse; 
import com.judebalance.backend.service.UserService;
import com.judebalance.backend.util.AesEncryptUtil;

import lombok.RequiredArgsConstructor;

/**
 * 로그인된 사용자의 프로필 조회 및 수정 컨트롤러
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화용
    private final AesEncryptUtil aesEncryptUtil;   // AES 복호화용
    private final UserService userService;

    /**
     * GET /api/user/me
     * - 현재 로그인한 사용자의 전체 프로필 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 username 가져오기
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        // 복호화된 정보로 프로필 반환
        UserProfileResponse profile = new UserProfileResponse(
            user.getId(),
            user.getUsername(),
            aesEncryptUtil.decrypt(user.getEmail()),
            user.getGender(),
            user.getAge(),
            user.getHeight(),
            user.getWeight(),
            user.getFitnessLevel(),
            user.getIsProfileSetupCompleted(),
            aesEncryptUtil.decrypt(user.getPhoneNumber())
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

        // 이메일 수정 (암호화 적용)
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            String encryptedEmail = aesEncryptUtil.encrypt(updateRequest.getEmail());
            boolean emailExists = userRepository.findByEmail(encryptedEmail).isPresent();
            if (emailExists && !encryptedEmail.equals(user.getEmail())) {
                return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
            }
            user.setEmail(encryptedEmail);
        }

        // 전화번호 수정 (암호화 적용)
        if (updateRequest.getPhoneNumber() != null && !updateRequest.getPhoneNumber().isEmpty()) {
            String encryptedPhone = aesEncryptUtil.encrypt(updateRequest.getPhoneNumber());
            boolean phoneExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getPhoneNumber() != null && u.getPhoneNumber().equals(encryptedPhone) && !u.getUsername().equals(username));
            if (phoneExists) {
                return ResponseEntity.badRequest().body("이미 사용 중인 전화번호입니다.");
            }
            user.setPhoneNumber(encryptedPhone);
        }

        // 비밀번호 수정 (암호화 후 저장)
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(user); // 수정사항 저장

        return ResponseEntity.ok("User updated successfully");
    }

    /**
     * POST /api/user/signup
     * - 회원가입 시 암호화 기반 중복 검사
     */
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> signup(@RequestBody RegisterRequest req) {
        String encryptedEmail = aesEncryptUtil.encrypt(req.getEmail());
        String encryptedPhone = aesEncryptUtil.encrypt(req.getPhoneNumber());

        // 중복 체크
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new RegisterResponse(null, null, "이미 존재하는 사용자명입니다."));
        }
        if (userRepository.findByEmail(encryptedEmail).isPresent()) {
            return ResponseEntity.badRequest().body(new RegisterResponse(null, null, "이미 사용 중인 이메일입니다."));
        }
        boolean phoneExists = userRepository.findAll().stream()
            .anyMatch(u -> u.getPhoneNumber() != null && u.getPhoneNumber().equals(encryptedPhone));
        if (phoneExists) {
            return ResponseEntity.badRequest().body(new RegisterResponse(null, null, "이미 사용 중인 전화번호입니다."));
        }

        return ResponseEntity.ok(new RegisterResponse("사용 가능", req.getEmail(), "가입 가능 상태입니다."));
    }

    /**
 * POST /api/user/profile/me
 * - 프로필 추가 정보 저장 및 설정 완료 처리
 */
@PostMapping("/profile/me")
public ResponseEntity<?> completeUserProfile(
        @RequestBody RegisterRequest req,
        Authentication authentication) {

    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

 

   
    // 정보 저장
    
    user.setNickname(req.getNickname());
   
    user.setGender(req.getGender());
    user.setAge(req.getAge());
    user.setHeight(req.getHeight());
    user.setWeight(req.getWeight());
    user.setFitnessLevel(req.getFitnessLevel());
    user.setIsProfileSetupCompleted(true);  // ✅ 핵심!

    userRepository.save(user);

    return ResponseEntity.ok(new RegisterResponse(
        user.getUsername(),
        aesEncryptUtil.decrypt(user.getEmail()),
        "프로필 정보가 성공적으로 저장되었습니다."
    ));
    }

    // UserController.java
    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(
            @RequestParam(name = "keyword") String keyword,
            Authentication authentication) {
    
        String currentUsername = authentication.getName(); // 현재 로그인한 사용자 제외
        List<UserSearchResponse> result = userService.searchByUsername(keyword, currentUsername);
        return ResponseEntity.ok(result);
    }
    

    @PostMapping("/change-password")
public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request,
                                             Authentication authentication) {
    String username = authentication.getName();
    userService.changePassword(username, request);
    return ResponseEntity.ok("비밀번호가 변경되었습니다.");
}

}
