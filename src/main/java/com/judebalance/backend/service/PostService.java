package com.judebalance.backend.service;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import main.java.com.judebalance.backend.response.PostResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시물 등록
     */
    public Post createPost(PostCreateRequest request, String username) {
        // 작성자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 게시물 저장
        Post post = Post.builder()
                .user(user)
                .content(request.getContent())
                .mediaUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }
    public List<PostResponse> getMyPosts(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자 없음"));

    return postRepository.findByUserId(user.getId()).stream()
        .map(post -> new PostResponse(
            post.getId(),
            post.getContent(),
            post.getMediaUrl(),
            post.getCreatedAt().toString()
        ))
        .toList();
    }

}
