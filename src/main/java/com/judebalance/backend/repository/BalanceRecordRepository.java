// src/main/java/com/judebalance/backend/repository/BalanceRecordRepository.java
package com.judebalance.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.judebalance.backend.domain.BalanceRecord;
import com.judebalance.backend.domain.User;

public interface BalanceRecordRepository extends JpaRepository<BalanceRecord, Long> {
    List<BalanceRecord> findByUser(User user);
    BalanceRecord findTopByUserIdAndFootOrderByDateDesc(Long userId, String foot);

     

    @Query("SELECT AVG(b.balanceScore) FROM BalanceRecord b WHERE b.user.id = :userId AND b.foot = :foot")
    double findAverageByUserAndFoot(@Param("userId") Long userId, @Param("foot") String foot);

    List<BalanceRecord> findTop3ByUserIdOrderByIdDesc(Long userId);

    List<BalanceRecord> findByUserIdOrderByDateDesc(Long userId);


}

