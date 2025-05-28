// src/main/java/com/judebalance/backend/controller/BalanceRecordController.java
package com.judebalance.backend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.BalanceRecordRequest;
import com.judebalance.backend.response.BalanceRecordResponse;
import com.judebalance.backend.response.CommonResponse;
import com.judebalance.backend.service.BalanceService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/balance")
public class BalanceRecordController {

    private final UserRepository userRepository;
    private final BalanceService balanceService;
    private final BalanceRecordRepository balanceRecordRepository;

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

     @GetMapping("/latest")
    public ResponseEntity<?> getLatestBalanceScore(
            @RequestParam("foot") String foot,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        BalanceRecord latestRecord = balanceRecordRepository
            .findTopByUserIdAndFootOrderByDateDesc(user.getId(), foot);

        if (latestRecord == null) {
            return ResponseEntity.ok(Collections.singletonMap("balanceScore", 0));
        }

        return ResponseEntity.ok(Collections.singletonMap("balanceScore", latestRecord.getBalanceScore()));
    }

@GetMapping("/records")
public ResponseEntity<List<BalanceRecordResponse>> getAllBalanceRecords(Authentication authentication) {
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    List<BalanceRecordResponse> result = balanceService.getAllBalanceRecords(user);

    return ResponseEntity.ok(result);
}


}
