package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private String mediaUrl;
    private String createdAt;
}
