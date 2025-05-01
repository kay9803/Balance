package com.judebalance.backend.service;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.UserProfileRequest;
import lombok.RequiredArgsConstructor;
import com.judebalance.backend.response.UserSearchResponse; 


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * 사용자 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateProfile(UserProfileRequest request) {
        // 여기서는 testuser 고정. (나중에 토큰 인증으로 바꿀 것)
        User user = userRepository.findByUsername("testuser")
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setGender(request.getGender());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setFitnessLevel(request.getFitnessLevel());

        userRepository.save(user);
    }
        // UserService.java
public List<UserSearchResponse> searchByNickname(String keyword, String currentUsername) {
    List<User> matched = userRepository.findByNicknameContainingIgnoreCase(keyword);
    return matched.stream()
            .filter(user -> !user.getUsername().equals(currentUsername)) // 자기자신 제외
            .map(user -> new UserSearchResponse(
                    user.getId(),
                    user.getNickname(),
                    user.getAge(),
                    "https://your-s3-or-default-image-url.com/default.png" // 예시
            ))
            .collect(Collectors.toList());
    }

     @Transactional
public void changePassword(String username, ChangePasswordRequest request) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    }


}
