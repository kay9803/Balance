package com.judebalance.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 프로필 업데이트 요청 DTO
 */
@Getter
@Setter
public class UserProfileRequest {
    private String gender;       // 성별
    private Integer age;         // 나이
    private Double height;       // 키 (cm)
    private Double weight;       // 몸무게 (kg)
    private String fitnessLevel; // 운동 수준 (초보/중급/고급)
    private String phoneNumber;
}