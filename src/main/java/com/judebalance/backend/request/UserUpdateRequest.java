// src/main/java/com/judebalance/backend/request/UserUpdateRequest.java
package com.judebalance.backend.request;

/**
 * 사용자 정보 수정 요청 DTO
 */
public class UserUpdateRequest {
    private String email;
    private String password;

    // 기본 생성자
    public UserUpdateRequest() {
    }

    // getter & setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
