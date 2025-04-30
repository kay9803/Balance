package com.judebalance.backend.controller;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.request.PostCreateRequest;
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

    /**
     * 게시물 등록 API
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequest request,
                                           Authentication authentication) {
        String username = authentication.getName();
        Post createdPost = postService.createPost(request, username);
        return ResponseEntity.ok(createdPost);
    }

    /**
     * 내가 쓴 게시물 리스트 조회 API
     */
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(Authentication authentication) {
        String username = authentication.getName();
        List<PostResponse> myPosts = postService.getMyPosts(username);
        return ResponseEntity.ok(myPosts);
    }
}
