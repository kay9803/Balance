package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인한 사용자의 프로필 정보를 담아 반환하는 DTO
 */
@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private final Long id;          // DB PK
    private final String username;  // 로그인 ID
    private final String email;     // 이메일 주소
}
