package com.judebalance.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class LikeController {

    private final LikeService likeService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("postId") Long postId, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        boolean liked = likeService.toggleLike(user, post);

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "liked", liked,
            "likeCount", likeService.getLikeCount(post)
        ));
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getLikeStatus(@PathVariable("postId") Long postId, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        boolean liked = likeService.hasUserLiked(user, post);
        long count = likeService.getLikeCount(post);

        return ResponseEntity.ok(Map.of(
            "liked", liked,
            "likeCount", count
        ));
    }
}
