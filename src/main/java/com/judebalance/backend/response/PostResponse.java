package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private String mediaUrl;
    private LocalDateTime createdAt;
}
