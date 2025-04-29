package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트로 반환할 응답 DTO
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;     // JWT 토큰
    private String username;  // 사용자 이름
}
