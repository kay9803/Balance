package com.judebalance.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.BalanceRecordRepository;
import com.judebalance.backend.request.BalanceRecordRequest;
import com.judebalance.backend.response.BalanceRecordResponse;

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

   public List<BalanceRecordResponse> getAllBalanceRecords(User user) {
    List<BalanceRecord> records = balanceRecordRepository
        .findByUserIdOrderByDateDesc(user.getId());
        return records.stream()
        .map(record -> new BalanceRecordResponse(
            record.getDate() != null
                ? record.getDate().toLocalDate().toString()
                : "날짜 없음",
            record.getFoot() != null
                ? record.getFoot()
                : "unknown",
            record.getBalanceScore() != null
                ? record.getBalanceScore()
                : 0,
            record.getDuration() != null
                ? record.getDuration()
                : 0
        ))
        .collect(Collectors.toList());
    
}


    
}
