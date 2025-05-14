package com.judebalance.backend.response;

import java.util.List;

import com.judebalance.backend.domain.AnalyzeRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeResponse {
    private List<AnalyzeRecord> records;
    private double leftScore;
    private double rightScore;
    private List<String> trainedAreas;
    private int percentile;
}
