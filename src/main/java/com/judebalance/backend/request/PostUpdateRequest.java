package com.judebalance.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
    private String content;   // 수정할 게시물 내용
    private String mediaUrl;  // 수정할 미디어 URL (선택)
}
