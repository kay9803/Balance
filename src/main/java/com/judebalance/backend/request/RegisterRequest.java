package com.judebalance.backend.request;

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
    private String gender;  // ✨ 성별 추가
    private String phoneNumber;
}
