// src/main/java/com/judebalance/backend/request/PasswordResetConfirmRequest.java
package com.judebalance.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 비밀번호 재설정 최종 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}
