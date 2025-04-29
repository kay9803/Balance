// src/main/java/com/judebalance/backend/util/JwtUtil.java
package com.judebalance.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸 클래스
 */
@Component
public class JwtUtil {

    // application.yml에 설정해둔 값들이 주입됩니다
    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.expiration-ms}")
    private long expirationMs;

    // 실제 서명에 사용할 Key 객체
    private Key key;

    @PostConstruct
    public void init() {
        // secret 문자열을 256비트 키로 변환
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** 토큰에서 subject(username)만 꺼내기 */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** 토큰에서 만료일(Expiration) 추출 */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** 공통 클레임 파싱 및 특정 클레임 추출용 헬퍼 */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** 토큰이 만료되었는지 여부 */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** 토큰이 특정 username과 일치하고 만료 안 됐는지 검증 */
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    /** 토큰의 모든 클레임 파싱 */
    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    /**
     * 토큰에 담긴 "roles" 클레임을 꺼내
     * Spring Security용 GrantedAuthority 리스트로 변환
     */
    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = parseAllClaims(token);
        Object rolesObject = claims.get("roles");

        if (rolesObject instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) rolesObject;
            return roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 신규 토큰 생성 메서드 (로그인 성공 시 사용)
     * @param username   subject로 들어갈 사용자 이름
     * @param roles      roles 클레임에 담을 문자열 리스트
     * @return 발급된 JWT
     */
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }
}
