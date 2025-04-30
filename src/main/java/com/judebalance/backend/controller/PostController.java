package com.judebalance.backend.controller;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.request.PostCreateRequest;
import com.judebalance.backend.request.PostUpdateRequest;
import com.judebalance.backend.response.PostResponse;
import com.judebalance.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // ✅ 게시물 등록
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequest request,
                                           Authentication authentication) {
        String username = authentication.getName();
        Post createdPost = postService.createPost(request, username);
        return ResponseEntity.ok(createdPost);
    }

    // ✅ 내가 쓴 게시물 조회
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(Authentication authentication) {
        String username = authentication.getName();
        List<PostResponse> myPosts = postService.getMyPosts(username);
        return ResponseEntity.ok(myPosts);
    }

    // ✅ 전체 게시물 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // ✅ 게시물 삭제

    // src/main/java/com/judebalance/backend/controller/PostController.java

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.deletePost(id, username);
        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

    // ✅ 게시물 수정
    public Post updatePost(Long postId, PostUpdateRequest request, String username) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }

        if (request.getMediaUrl() != null) {
            post.setMediaUrl(request.getMediaUrl());
        }

        return postRepository.save(post);
    }

    // ✅ 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
                                           @RequestBody PostUpdateRequest request,
                                           Authentication authentication) {
        String username = authentication.getName();
        Post updated = postService.updatePost(id, request, username);
        return ResponseEntity.ok(updated);
    }

}
