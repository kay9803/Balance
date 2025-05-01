// src/main/java/com/judebalance/backend/response/RegisterResponse.java
package com.judebalance.backend.response;

import com.judebalance.backend.request.RegisterRequest;
import com.judebalance.backend.response.RegisterResponse;
import com.judebalance.backend.request.PasswordResetConfirmRequest;
import com.judebalance.backend.request.ChangePasswordRequest;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterResponse {
    private String username;
    private String email;
    private String message;
}