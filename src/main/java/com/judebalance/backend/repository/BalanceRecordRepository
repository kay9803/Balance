// src/main/java/com/judebalance/backend/repository/BalanceRecordRepository.java
package com.judebalance.backend.repository;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRecordRepository extends JpaRepository<BalanceRecord, Long> {
    List<BalanceRecord> findByUser(User user);
}
