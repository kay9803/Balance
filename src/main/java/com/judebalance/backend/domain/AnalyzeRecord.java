package com.judebalance.backend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeRecord {

    @JsonProperty("time")
    private int time; 

    @JsonProperty("intensity")              // 운동 시간 (초)
    private double intensity;   
    
    @JsonProperty("score")// 운동 강도 점수
    private Integer balanceScore;   // ✅ 균형 점수 (nullable 허용)
}
