package com.judebalance.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.request.BalanceRecordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRecordRepository balanceRecordRepository;

    /**
     * 🔹 점수 기반으로 기록 저장
     */
    public void saveBalanceRecord(User user, BalanceRecordRequest request) {
        BalanceRecord record = BalanceRecord.builder()
            .user(user)
            .balanceScore(request.getBalanceScore())  // ✅ 점수만 저장
            .duration(request.getDuration()) 
            .date(LocalDateTime.now())
            .build();

        balanceRecordRepository.save(record);
    }

    /**
     * 🔹 연령/성별 기준 대비 사용자 시간 점수를 계산
     */
    public int calculateBalanceScore(double userTime, int age, String gender) {
        double reference = getReferenceAverage(age, gender);

        double rawScore = (userTime / reference) * 100;
        rawScore = Math.max(10, Math.min(rawScore, 120));  // 점수 범위 클리핑

        return (int) Math.round((rawScore / 120.0) * 100);  // 0~100 정규화
    }

    /**
     * 🔹 연령/성별에 따른 기준 유지 시간 반환
     */
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

        return 5.0; // 🔸 fallback 기본값
    }
}
