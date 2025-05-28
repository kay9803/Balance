package com.judebalance.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // 사용자 ID

    @Column(nullable = false)
    private String password;  // 해시된 비밀번호

    @Column(nullable = false, unique = true)
    private String email;     // 사용자 이메일

    @Column(nullable = false)
    private String gender;    // 성별

    @Column(nullable = false)
    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private String nickname;  // 닉네임

    

    @Column(nullable = false)
    private Integer age;       // 나이

    @Column(nullable = false)
    private Double height;     // 키 (cm)

    @Column(nullable = false)
    private Double weight;     // 몸무게 (kg)

    @Column(nullable = false)
    private String fitnessLevel;  // 운동 수준 (초보/중급/고급)

    @Builder.Default
    @Column(nullable = false)
    private Boolean isProfileSetupCompleted = false;  // 프로필 설정 완료 여부
}
