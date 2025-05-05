// src/main/java/com/judebalance/backend/controller/BalanceRecordController.java
package com.judebalance.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.BalanceRecordRequest;
import com.judebalance.backend.response.CommonResponse;
import com.judebalance.backend.service.BalanceService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/balance")
public class BalanceRecordController {

    private final UserRepository userRepository;
    private final BalanceService balanceService;

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveBalanceRecord(
            @RequestBody BalanceRecordRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        balanceService.saveBalanceRecord(user, request);

        return ResponseEntity.ok(new CommonResponse("균형 기록이 저장되었습니다."));
    }
}
