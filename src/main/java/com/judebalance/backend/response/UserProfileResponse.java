package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 프로필 조회 응답 DTO
 */
@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String gender;             // 성별
    private final Integer age;                // 나이
    private final Double height;              // 키
    private final Double weight;              // 몸무게
    private final String fitnessLevel;        // 운동 수준
    private final Boolean isProfileSetupCompleted;  // 프로필 설정 완료 여부
    private String phoneNumber;
}

