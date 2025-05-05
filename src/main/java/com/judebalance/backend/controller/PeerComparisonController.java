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
import com.judebalance.backend.service.BalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analyze")
@RequiredArgsConstructor
public class PeerComparisonController {

    private final UserRepository userRepository;
    private final BalanceRecordRepository balanceRecordRepository;
    private final BalanceService balanceService;

    @GetMapping("/comparison")
    public ResponseEntity<PeerComparisonResponse> getComparison(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<BalanceRecord> myRecords = balanceRecordRepository.findByUser(user);
        double myAverage = myRecords.stream()
            .filter(r -> r.getBalanceScore() != null)
            .mapToInt(BalanceRecord::getBalanceScore)
            .average().orElse(0);

        int age = user.getAge();
        String gender = user.getGender();

        // 기준 점수 환산
        double referenceAverageTime = getReferenceAverage(age, gender);
        double referenceScore = balanceService.calculateBalanceScore(referenceAverageTime, age, gender);

        List<BalanceRecord> groupRecords = balanceRecordRepository.findAll().stream()
            .filter(r -> r.getUser() != null)
            .filter(r -> r.getBalanceScore() != null)
            .filter(r -> r.getUser().getGender().equals(gender))
            .filter(r -> getAgeGroup(r.getUser().getAge()) == getAgeGroup(age))
            .toList();

        double dbPeerAverage = groupRecords.stream()
            .mapToInt(BalanceRecord::getBalanceScore)
            .average().orElse(0);

        double peerAverage = groupRecords.size() >= 50 ? dbPeerAverage : referenceScore;

        long betterThan = groupRecords.stream()
            .filter(r -> r.getBalanceScore() < myAverage)
            .count();

        int percentile = groupRecords.size() == 0 ? 100 :
            (int) ((double) betterThan / groupRecords.size() * 100);

        return ResponseEntity.ok(new PeerComparisonResponse(myAverage, peerAverage, percentile));
    }

    private int getAgeGroup(int age) {
        return (age / 10) * 10;
    }

    private double getReferenceAverage(int age, String gender) {
        boolean isMale = gender.equalsIgnoreCase("male");

        if (age >= 18 && age <= 39) {
            return isMale ? 16.9 : 13.1;
        } else if (age >= 40 && age <= 49) {
            return isMale ? 12.0 : 13.5;
        } else if (age >= 50 && age <= 59) {
            return isMale ? 8.6 : 7.9;
        } else if (age >= 60 && age <= 69) {
            return isMale ? 5.1 : 3.6;
        } else if (age >= 70 && age <= 79) {
            return isMale ? 2.6 : 3.7;
        } else if (age >= 80 && age <= 99) {
            return isMale ? 1.8 : 2.1;
        }

        return 5.0; // 🔸 나이 범위에 포함되지 않는 경우의 보수적 기본값
    }
}
