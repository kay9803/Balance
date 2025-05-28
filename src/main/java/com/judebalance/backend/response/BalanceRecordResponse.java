package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceRecordResponse {
    private String date;         // 형식: "2025-05-22"
    private String foot;         // "left" or "right"
    private int balanceScore;
    private int duration;        // 유지 시간 (초)
}
