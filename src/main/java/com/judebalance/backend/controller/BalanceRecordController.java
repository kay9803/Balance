// src/main/java/com/judebalance/backend/controller/BalanceRecordController.java
package com.judebalance.backend.controller;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.BalanceRecordRequest;
import com.judebalance.backend.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceRecordController {

    private final UserRepository userRepository;
    private final BalanceRecordRepository balanceRecordRepository;

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveBalanceRecord(
            @RequestBody BalanceRecordRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        BalanceRecord record = BalanceRecord.builder()
            .user(user)
            .duration(request.getBalanceTime())
            .date(LocalDate.now()) // 오늘 날짜로 저장
            .build();

        balanceRecordRepository.save(record);

        return ResponseEntity.ok(new CommonResponse("균형 기록이 저장되었습니다."));
    }
}
