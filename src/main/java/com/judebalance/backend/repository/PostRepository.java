package com.judebalance.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.judebalance.backend.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIsPublicTrueOrderByCreatedAtDesc();
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
}

