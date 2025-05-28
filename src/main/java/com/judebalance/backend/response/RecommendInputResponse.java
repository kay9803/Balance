package com.judebalance.backend.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendInputResponse {
    private List<Integer> recentScores;
    private double avgIntensity;
    private int totalDuration;
    private String focusArea;

    private int weeklyWorkoutCount;
    private List<String> history;
 
}
