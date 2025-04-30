package com.judebalance.backend.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/predict")
    public ResponseEntity<?> getPrediction(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 최근 기록 내림차순 정렬 후 최대 3개 사용
        List<BalanceRecord> records = balanceRecordRepository.findByUser(user);
        records.sort(Comparator.comparing(BalanceRecord::getDate).reversed());

        // BalanceRecordRequest 리스트로 변환
        List<BalanceRecordRequest> dtoList = records.stream().limit(3).map(r -> {
            BalanceRecordRequest dto = new BalanceRecordRequest();
            dto.setDate(r.getDate().toString());
            dto.setBalance_time(r.getBalance_time());  // duration → balance_time 필드로 바뀐 경우
            return dto;
        }).collect(Collectors.toList());

        // FastAPI에 예측 요청
        String predictionResult = predictionService.requestPrediction(user.getId(), dtoList);

        return ResponseEntity.ok(predictionResult);
    }
}
