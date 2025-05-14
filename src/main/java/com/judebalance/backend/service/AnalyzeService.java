package com.judebalance.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.AnalyzeRecord;
import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.response.AnalyzeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

    private final WorkoutRecordRepository workoutRecordRepository;
    private final BalanceRecordRepository balanceRecordRepository;

    public AnalyzeResponse getRecentAnalysis(User user) {
        // ✅ 최근 workout 3회
        List<WorkoutRecord> recentWorkouts = workoutRecordRepository
            .findTop3ByUserOrderByDateDesc(user);

        // ✅ 최근 balance 3회 (score용) - createdAt 대신 id 정렬 사용
        List<BalanceRecord> recentBalances = balanceRecordRepository
            .findTop3ByUserIdOrderByIdDesc(user.getId());

        // ✅ balance 점수 추출 (최신순 정렬되었음)
        List<Integer> balanceScores = recentBalances.stream()
            .map(BalanceRecord::getBalanceScore)
            .toList();

        // ✅ 분석 레코드 구성
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

        // ✅ 평균 점수
        double leftScore = balanceRecordRepository.findAverageByUserAndFoot(user.getId(), "left");
        double rightScore = balanceRecordRepository.findAverageByUserAndFoot(user.getId(), "right");

        // ✅ 임시 데이터
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
}
