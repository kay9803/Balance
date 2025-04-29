// src/main/java/com/judebalance/backend/request/WorkoutRecordRequest.java
package com.judebalance.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 기록 저장 요청용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkoutRecordRequest {

    private String exerciseName;  // 운동 이름
    private Integer duration;     // 운동 시간 (분)

    // ✨ 날짜(date)는 이제 클라이언트가 안 보내도 됨! (자동으로 LocalDate.now()로 처리할 거라서 삭제)
}
