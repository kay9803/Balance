package com.judebalance.backend.controller;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.request.PostCreateRequest;
import com.judebalance.backend.request.PostUpdateRequest;
import com.judebalance.backend.response.PostResponse;
import com.judebalance.backend.service.PostService;
import com.judebalance.backend.service.LikeService;

import lombok.RequiredArgsConstructor;
import main.java.com.judebalance.backend.request.CommentRequest;
import main.java.com.judebalance.backend.response.CommentResponse;
import main.java.com.judebalance.backend.service.CommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시물 등록, 조회, 수정, 삭제 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;


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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.deletePost(id, username);
        return ResponseEntity.ok("게시물이 삭제되었습니다.");
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

    // ✅ 게시물 좋아요 등록
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id,
                                           Authentication authentication) {
        String username = authentication.getName();
        likeService.likePost(id, username);
        return ResponseEntity.ok("좋아요 완료");
    }

    // ✅ 게시물 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> unlikePost(@PathVariable Long id,
                                             Authentication authentication) {
        String username = authentication.getName();
        likeService.unlikePost(id, username);
        return ResponseEntity.ok("좋아요 취소 완료");
    }

    // ✅ 댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long id,
                                         @RequestBody CommentRequest request,
                                         Authentication authentication) {
         String username = authentication.getName();
        commentService.createComment(id, username, request);
        return ResponseEntity.ok("댓글이 작성되었습니다.");
    }

// ✅ 댓글 조회
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
        List<CommentResponse> comments = commentService.getComments(id);
        return ResponseEntity.ok(comments);
    }

}
