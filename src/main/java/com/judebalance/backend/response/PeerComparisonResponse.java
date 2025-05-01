package com.judebalance.backend.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeerComparisonResponse {
    private double myAverage;
    private double peerAverage;
    private int percentile; // 예: 상위 80%
    
}
