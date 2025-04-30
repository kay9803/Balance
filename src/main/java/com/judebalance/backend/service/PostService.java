package com.judebalance.backend.service;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.PostCreateRequest;
import com.judebalance.backend.request.PostUpdateRequest;
import com.judebalance.backend.response.PostResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 내 게시물 조회
     */
    public List<PostResponse> getMyPosts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return postRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getMediaUrl(),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 전체 게시물 조회
     */
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getContent(),
                        post.getMediaUrl(),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 게시물 수정
     */
    public Post updatePost(Long postId, PostUpdateRequest request, String username) {
        // 1. 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        // 2. 권한 확인 (작성자 본인만 수정 가능)
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 3. 내용 수정
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }

        // 4. 미디어 URL 수정
        if (request.getMediaUrl() != null) {
            post.setMediaUrl(request.getMediaUrl());
        }

        // 5. 저장 및 반환
        return postRepository.save(post);
    }

    /**
     * 게시물 삭제
     */
    public void deletePost(Long id, String username) {
        // 1. 게시물 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        // 2. 권한 확인 (작성자 본인만 삭제 가능)
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 3. 삭제
        postRepository.delete(post);
    }
}