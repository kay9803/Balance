// src/main/java/com/judebalance/backend/request/BalanceRecordRequest.java
package com.judebalance.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BalanceRecordRequest {
    private Integer duration; // 초 단위 기록
}

