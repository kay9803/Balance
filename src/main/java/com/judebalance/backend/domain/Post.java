package com.judebalance.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.judebalance.backend.domain.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 작성자
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    private String mediaUrl; // 이미지나 영상 링크

    private int comments;
    private int views;

    @Column(nullable = false)
    private int likeCount = 0;  // 게시물 좋아요 수

    private LocalDateTime createdAt;
}
