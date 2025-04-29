package com.judebalance.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 정보를 저장하는 엔티티 클래스.
 * users 테이블과 매핑됨.
 */
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
    private Long id;  // 기본 키

    @Column(nullable = false, unique = true)
    private String username;  // 사용자 ID

    @Column(nullable = false)
    private String password;  // 해시된 비밀번호

    @Column(nullable = false, unique = true)
    private String email;  // 사용자 이메일

    @Column(nullable = false)
    private String gender;  // 사용자 성별 ("남자" 또는 "여자")

    @Column(nullable = false)
    private String phoneNumber;  // 사용자 전화번호
}
