package com.judebalance.backend.repository;

import com.judebalance.backend.domain.Like;
import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
