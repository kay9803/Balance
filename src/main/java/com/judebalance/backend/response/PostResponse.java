package com.judebalance.backend.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
    private String username;
    private String exerciseName;
    private String content;
    private LocalDateTime createdAt;
    private Long id;
}