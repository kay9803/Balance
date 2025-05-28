// src/main/java/com/judebalance/backend/request/WorkoutRecordRequest.java
package com.judebalance.backend.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 기록 저장 요청용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class WorkoutRecordRequest {
    private String exerciseName;
    private int totalTime;
    private int completedSets;
    private String feedback;
    private String memo;
    private String visibility;
    private double intensityScore;
    private String date;
}