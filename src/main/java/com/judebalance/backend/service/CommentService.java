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
    public void createComment(Long postId,
