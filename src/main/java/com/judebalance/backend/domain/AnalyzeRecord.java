package com.judebalance.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeRecord {

    private int time;               // 운동 시간 (초)
    private double intensity;       // 운동 강도 점수
    private Integer balanceScore;   // ✅ 균형 점수 (nullable 허용)
}
