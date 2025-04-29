// src/main/java/com/judebalance/backend/response/RegisterResponse.java
package com.judebalance.backend.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterResponse {
    private String username;
    private String email;
    private String message;
}