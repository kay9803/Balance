// src/main/java/com/judebalance/backend/repository/WorkoutRecordRepository.java
package com.judebalance.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;

/**
 * WorkoutRecord 엔티티를 관리하는 JPA 리포지토리
 */
public interface WorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {

    // 특정 사용자의 운동 기록만 조회 (기존 메소드)
    List<WorkoutRecord> findByUser(User user);

    // 특정 사용자의 운동 기록을 날짜 기준 내림차순(최신순)으로 조회 (새로 추가하는 메소드)
    List<WorkoutRecord> findByUserOrderByDateDesc(User user);

    List<WorkoutRecord> findTop3ByUserOrderByDateDesc(User user);

    List<WorkoutRecord> findByUserAndDateAfter(User user, LocalDate date);

   


}
