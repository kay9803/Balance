package com.judebalance.backend.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시물 작성 요청 DTO
 */
@Getter
@Setter
public class PostCreateRequest {
    private String content;     // 텍스트 내용
    private String imageUrl;    // 이미지 URL (선택사항)
}
