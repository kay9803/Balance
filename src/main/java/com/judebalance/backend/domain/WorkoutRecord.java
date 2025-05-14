// src/main/java/com/judebalance/backend/domain/WorkoutRecord.java
package com.judebalance.backend.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 기록을 저장하는 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String exerciseName;

    private Integer duration;  // 초 or 분

    private Integer completedSets;  // ✅ 추가됨

    private LocalDate date;

    @Column(name = "intensity_score")
    private double intensityScore;

    @Column(length = 20)
    private String feedback;

    @Column(length = 255)
    private String memo;  // ✅ 추가됨

    @Column(length = 20)
    private String visibility;  // ✅ public / private
}