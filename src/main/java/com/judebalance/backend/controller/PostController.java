package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.response.PostResponse;
import com.judebalance.backend.service.PostService;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    /**
     * 커뮤니티 포스트 조회 API
     * @return
     */


    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPublicPosts() {
        List<PostResponse> posts = postService.getAllPublicPostResponses();
        return ResponseEntity.ok(posts);
    }
}


