package com.judebalance.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청에서 들어오는 JSON 데이터를 받는 DTO
 */
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
