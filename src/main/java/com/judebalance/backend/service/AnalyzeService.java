package com.judebalance.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.AnalyzeRecord;
import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.response.AnalyzeResponse;
import com.judebalance.backend.response.RecommendInputResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

    private final WorkoutRecordRepository workoutRecordRepository;
    private final BalanceRecordRepository balanceRecordRepository;

    private static final Map<String, String> exerciseFocusMap = Map.ofEntries(
        Map.entry("의자 스쿼트", "하체"),
        Map.entry("발끝 들기", "하체"),
        Map.entry("뒤꿈치 들기", "하체"),
        Map.entry("버드독", "코어"),
        Map.entry("벽 짚고 플랭크", "코어"),
        Map.entry("사이드 스텝", "전신"),
        Map.entry("서서 하는 트위스트", "코어"),
        Map.entry("스탠딩 사이드 레그 리프트", "하체"),
        Map.entry("스탠딩 힙 익스텐션", "하체"),
        Map.entry("싱글 레그 스탠딩", "균형"),
        Map.entry("어깨 스트레칭", "유연성"),
        Map.entry("제자리 걷기", "전신"),
        Map.entry("종아리 스트레칭", "유연성"),
        Map.entry("좌우 스탠딩 크런치", "코어"),
        Map.entry("허리 회전 스트레칭", "유연성")
    );

    public AnalyzeResponse getRecentAnalysis(User user) {
        List<WorkoutRecord> recentWorkouts = workoutRecordRepository.findTop3ByUserOrderByDateDesc(user);
        List<BalanceRecord> recentBalances = balanceRecordRepository.findTop3ByUserIdOrderByIdDesc(user.getId());

        List<Integer> balanceScores = recentBalances.stream()
            .map(BalanceRecord::getBalanceScore)
            .toList();

        List<AnalyzeRecord> analyzeRecords = new ArrayList<>();
        for (int i = 0; i < recentWorkouts.size(); i++) {
            WorkoutRecord workout = recentWorkouts.get(i);
            Integer score = (i < balanceScores.size()) ? balanceScores.get(i) : null;
            analyzeRecords.add(new AnalyzeRecord(
                workout.getDuration(),
                workout.getIntensityScore(),
                score
            ));
        }

        double leftScore = balanceRecordRepository.findAverageByUserAndFoot(user.getId(), "left");
        double rightScore = balanceRecordRepository.findAverageByUserAndFoot(user.getId(), "right");

        List<String> trainedAreas = List.of("하체", "코어", "어깨");
        int percentile = 82;

        return new AnalyzeResponse(
            analyzeRecords,
            leftScore,
            rightScore,
            trainedAreas,
            percentile
        );
    }

    public RecommendInputResponse getRecommendInput(User user) {
        List<WorkoutRecord> workouts = workoutRecordRepository.findTop3ByUserOrderByDateDesc(user);
        List<BalanceRecord> balances = balanceRecordRepository.findTop3ByUserIdOrderByIdDesc(user.getId());

        List<Integer> recentScores = balances.stream()
            .map(BalanceRecord::getBalanceScore)
            .collect(Collectors.toList());

        double avgIntensity = workouts.stream()
            .mapToDouble(WorkoutRecord::getIntensityScore)
            .average().orElse(0.7);

        int totalDuration = workouts.stream()
            .mapToInt(WorkoutRecord::getDuration)
            .sum();

        Map<String, Long> areaCounts = workouts.stream()
            .map(r -> exerciseFocusMap.getOrDefault(r.getExerciseName(), "전신"))
            .collect(Collectors.groupingBy(area -> area, Collectors.counting()));

        String focusArea = areaCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("전신");

        return new RecommendInputResponse(recentScores, avgIntensity, totalDuration, focusArea);
    }
}
