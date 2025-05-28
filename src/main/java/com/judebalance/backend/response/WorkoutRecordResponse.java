// src/main/java/com/judebalance/backend/response/WorkoutRecordResponse.java
package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 운동 기록 조회 응답용 DTO
 */
@Data
@AllArgsConstructor
public class WorkoutRecordResponse {
    private String exerciseName;
    private int duration;
    private int completedSets;
    private String feedback;
    private double intensityScore;
    private String date; // ISO 포맷 문자열

    
}

