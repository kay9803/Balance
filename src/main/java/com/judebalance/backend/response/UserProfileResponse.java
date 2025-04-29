package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 프로필 조회 응답 DTO
 */
@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String gender;
    private Integer age;
    private Double height;
    private Double weight;
    private String fitnessLevel;
}
@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final Boolean isProfileSetupCompleted;  // ✅ 추가
}
