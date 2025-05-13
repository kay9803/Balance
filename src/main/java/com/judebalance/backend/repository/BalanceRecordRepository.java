// src/main/java/com/judebalance/backend/repository/BalanceRecordRepository.java
package com.judebalance.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;

public interface BalanceRecordRepository extends JpaRepository<BalanceRecord, Long> {
    List<BalanceRecord> findByUser(User user);
    BalanceRecord findTopByUserIdAndFootOrderByDateDesc(Long userId, String foot);
}
