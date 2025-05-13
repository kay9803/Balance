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
            .foot(request.getFoot())  // ✅ foot 정보 저장
            .build();

        balanceRecordRepository.save(record);
    }

    
}
