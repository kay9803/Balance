package com.judebalance.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT 인증 필터가 잘 동작하는지 확인용 컨트롤러
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok("✅ Secure endpoint reached!");
    }

    
}