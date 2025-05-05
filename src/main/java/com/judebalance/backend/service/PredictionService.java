package com.judebalance.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.judebalance.backend.request.BalanceRecordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String requestPrediction(Long userId, List<BalanceRecordRequest> history) {
        // ✅ 1. 보정된 점수 리스트 생성
        List<Map<String, Object>> adjustedHistory = history.stream()
            .map(record -> {
                int rawScore = record.getBalanceScore();
                int duration = record.getDuration(); // <- duration 필드 필요
                double ratio = Math.min(duration / 20.0, 1.0);
                int adjustedScore = (int) Math.round(rawScore * ratio);
    
                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("date", record.getDate());
                recordMap.put("balanceScore", adjustedScore); // ✅ 보정된 점수만 전달
    
                return recordMap;
            })
            .toList();
    
        // ✅ 2. FastAPI 요청용 JSON 구성
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("balance_history", adjustedHistory);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
    
        System.out.println("📤 FastAPI 전송 데이터: " + body);
    
        // ✅ 3. 요청 전송
        String fastApiUrl = "https://7232-34-55-200-180.ngrok-free.app/predict";
        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, entity, String.class);
        return response.getBody();
    }
    
}
