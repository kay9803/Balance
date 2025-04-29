// src/main/java/com/judebalance/backend/domain/WorkoutRecord.java
package com.judebalance.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * 운동 기록을 저장하는 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class WorkoutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 운동 기록 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 운동 기록을 남긴 사용자

    private String exerciseName;  // 운동 이름 (예: 스쿼트, 푸쉬업)

    private Integer duration;  // 운동 시간 (분 단위)

    private LocalDate date;  // 운동한 날짜
}
