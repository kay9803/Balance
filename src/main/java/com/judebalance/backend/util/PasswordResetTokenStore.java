// src/main/java/com/judebalance/backend/util/PasswordResetTokenStore.java
package com.judebalance.backend.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 비밀번호 재설정 토큰 저장소
 */
@Component
public class PasswordResetTokenStore {

    @Getter
    private static class TokenInfo {
        private final String email;
        private final LocalDateTime expiresAt;

        public TokenInfo(String email, LocalDateTime expiresAt) {
            this.email = email;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, new TokenInfo(email, LocalDateTime.now().plusMinutes(30)));
    
        System.out.println("[DEBUG] 발급된 비밀번호 재설정 토큰: " + token);  // ✨ 이 줄 추가
    
        return token;
    }
    

    public String getEmailIfValid(String token) {
        TokenInfo info = tokenStore.get(token);
        if (info == null || LocalDateTime.now().isAfter(info.expiresAt)) {
            return null;
        }
        return info.email;
    }

    public void remove(String token) {
        tokenStore.remove(token);
    }
}
