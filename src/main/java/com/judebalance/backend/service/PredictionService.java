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
        // ✅ FastAPI에 전달할 JSON 구성
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("balance_history", history);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        
        
        System.out.println("📤 FastAPI 전송 데이터: " + body);
        // 🔗 FastAPI ngrok 주소
        String fastApiUrl = "https://93b4-34-72-43-134.ngrok-free.app/predict"; // 🔁 실제 ngrok 주소로 바꿔줘

        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, entity, String.class);
      
        return response.getBody();
    }
}
