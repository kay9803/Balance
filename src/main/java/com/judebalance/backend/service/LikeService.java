package com.judebalance.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.Like;
import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public boolean toggleLike(User user, Post post) {
        Optional<Like> existing = likeRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false; // 좋아요 취소됨
        } else {
            Like like = Like.builder()
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
            likeRepository.save(like);
            return true; // 좋아요 추가됨
        }
    }

    public long getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }

    public boolean hasUserLiked(User user, Post post) {
        return likeRepository.existsByUserAndPost(user, post);
    }
}

