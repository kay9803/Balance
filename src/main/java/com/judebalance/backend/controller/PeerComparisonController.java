package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.response.PeerComparisonResponse;

import lombok.RequiredArgsConstructor;
// PeerComparisonController.java
@RestController
@RequestMapping("/api/analyze")
@RequiredArgsConstructor
public class PeerComparisonController {

    private final UserRepository userRepository;
    private final BalanceRecordRepository balanceRecordRepository;

    @GetMapping("/comparison")
    public ResponseEntity<PeerComparisonResponse> getComparison(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<BalanceRecord> myRecords = balanceRecordRepository.findByUser(user);
        double myAverage = myRecords.stream()
            .filter(r -> r.getBalance_time() != null)
            .mapToInt(BalanceRecord::getBalance_time)
            .average().orElse(0);

        List<BalanceRecord> allRecords = balanceRecordRepository.findAll();
        double peerAverage = allRecords.stream()
            .filter(r -> !r.getUser().equals(user) && r.getBalance_time() != null)
            .mapToInt(BalanceRecord::getBalance_time)
            .average().orElse(0);

        long betterThanCount = allRecords.stream()
            .filter(r -> r.getUser() != null && !r.getUser().equals(user))
            .filter(r -> r.getBalance_time() != null && r.getBalance_time() < myAverage)
            .count();
        long peerCount = allRecords.stream()
            .filter(r -> r.getUser() != null && !r.getUser().equals(user))
            .count();

        int percentile = peerCount == 0 ? 100 : (int) ((double) betterThanCount / peerCount * 100);

        return ResponseEntity.ok(new PeerComparisonResponse(myAverage, peerAverage, percentile));
    }
}
