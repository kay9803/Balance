// src/main/java/com/judebalance/backend/request/BalanceRecordRequest.java
package com.judebalance.backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRecordRequest {
    private String date;
    private Integer balanceScore;
    private Integer duration;  
    private String foot; 
}

