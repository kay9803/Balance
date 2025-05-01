package com.judebalance.backend.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.BalanceRecordRequest;
import com.judebalance.backend.service.PredictionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiPredictionController {

    private final UserRepository userRepository;
    private final BalanceRecordRepository balanceRecordRepository;
    private final PredictionService predictionService;

    @PostMapping("/predict")
    public ResponseEntity<?> getPrediction(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<BalanceRecord> records = balanceRecordRepository.findByUser(user).stream()
            .filter(r -> r.getBalance_time() != null) // null 제거
            .sorted(Comparator.comparing(BalanceRecord::getDate).reversed())
            .limit(3)
            .collect(Collectors.toList());

        List<BalanceRecordRequest> dtoList = records.stream().map(r -> {
            BalanceRecordRequest dto = new BalanceRecordRequest();
            dto.setDate(r.getDate().toString());
            dto.setBalance_time(r.getBalance_time());  // null 아닌 값만 옴
            return dto;
        }).collect(Collectors.toList());

        String predictionResult = predictionService.requestPrediction(user.getId(), dtoList);
        return ResponseEntity.ok(predictionResult);
    }
}