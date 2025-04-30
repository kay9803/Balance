package com.judebalance.backend.repository;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 유저 ID로 게시물 조회 (옵션)
    List<Post> findByUserId(Long userId);

    // 최신순 정렬 (옵션)
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserOrderByCreatedAtDesc(User user);

}
