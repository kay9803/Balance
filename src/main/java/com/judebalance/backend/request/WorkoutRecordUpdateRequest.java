// src/main/java/com/judebalance/backend/request/WorkoutRecordUpdateRequest.java
package com.judebalance.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 기록 수정 요청용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkoutRecordUpdateRequest {

    private String exerciseName;  // 수정할 운동 이름
    private Integer duration;     // 수정할 운동 시간 (분)
}
