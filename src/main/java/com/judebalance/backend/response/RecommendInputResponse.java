package com.judebalance.backend.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendInputResponse {
    private List<Integer> recent_scores;
    private double recent_intensity_avg;
    private int recent_duration_sum;
    private String focus_area;
}
