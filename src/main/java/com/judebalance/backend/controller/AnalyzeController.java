package com.judebalance.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.response.AnalyzeResponse;
import com.judebalance.backend.response.RecommendInputResponse;
import com.judebalance.backend.service.AnalyzeService;

import lombok.RequiredArgsConstructor;

/**
 * 운동 분석 관련 컨트롤러
 */
@RestController
@RequestMapping("/api/analyze")
@RequiredArgsConstructor
public class AnalyzeController {

    private final AnalyzeService analyzeService;
    private final UserRepository userRepository;

    /**
     * 최근 3회 운동 분석 데이터 조회
     */
    @GetMapping("/recent3")
    public ResponseEntity<AnalyzeResponse> getRecentAnalyzeData(Authentication authentication) {
        String username = authentication.getName();  // 현재 로그인한 사용자의 username
    
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    
        AnalyzeResponse response = analyzeService.getRecentAnalysis(user);
        return ResponseEntity.ok(response);
    }
    /**
     * 운동 추천 입력 데이터 조회
     */
    @GetMapping("/recommend-input")
     public ResponseEntity<RecommendInputResponse> getRecommendationInput(Authentication authentication) {
     String username = authentication.getName();
     User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
     return ResponseEntity.ok(analyzeService.getRecommendInput(user));
}
    
    
}
