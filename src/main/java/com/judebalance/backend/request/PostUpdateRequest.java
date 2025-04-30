// src/main/java/com/judebalance/backend/request/PostUpdateRequest.java
package com.judebalance.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 수정 요청 DTO
 */
@Getter
@Setter
public class PostUpdateRequest {
    private String content;     // 새 게시물 내용
    private String mediaUrl;    // 새 이미지 URL
}
