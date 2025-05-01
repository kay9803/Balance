package com.judebalance.backend.request;

import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.request.PasswordResetConfirmRequest;
import com.judebalance.backend.request.ChangePasswordRequest;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String gender;
    private String phoneNumber;
    private String nickname;      // 닉네임 추가
    private String name;           // 이름 추가
    private Integer age;           // 나이 추가
    private Double height;         // 키 추가
    private Double weight;         // 몸무게 추가
    private String fitnessLevel;   // 운동 수준 추가 (초보/중급/고급)
}
