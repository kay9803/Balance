package com.judebalance.backend.service;

import com.judebalance.backend.domain.Comment;
import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.CommentRepository;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.request.CommentRequest;
import com.judebalance.backend.response.CommentResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    @Transactional
    public void createComment(Long postId, String username, CommentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 조회
     */
    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        return commentRepository.findByPostOrderByCreatedAtDesc(post)
                .stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getNickname(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
