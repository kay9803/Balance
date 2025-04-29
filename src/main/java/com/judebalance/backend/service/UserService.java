package com.judebalance.backend.service;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.UserProfileRequest;
import lombok.RequiredArgsConstructor;
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
}
