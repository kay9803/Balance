// src/main/java/com/judebalance/backend/response/WorkoutRecordResponse.java
package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 운동 기록 조회 응답용 DTO
 */
@Getter
@AllArgsConstructor
public class WorkoutRecordResponse {

    private Long id;               // 운동 기록 ID
    private String exerciseName;   // 운동 이름
    private Integer duration;      // 운동 시간 (분)
    private LocalDate date;         // 운동 날짜
}
