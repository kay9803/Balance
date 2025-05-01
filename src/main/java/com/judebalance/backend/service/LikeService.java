package com.judebalance.backend.service;

import com.judebalance.backend.domain.Like;
import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.LikeRepository;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요 등록
     */
    @Transactional
    public void likePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        boolean alreadyLiked = likeRepository.findByUserAndPost(user, post).isPresent();
        if (alreadyLiked) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        Like like = Like.builder()
            .user(user)
            .post(post)
            .build();

        likeRepository.save(like);
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void unlikePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        Like like = likeRepository.findByUserAndPost(user, post)
            .orElseThrow(() -> new RuntimeException("좋아요를 누른 적이 없습니다."));

        likeRepository.delete(like);
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));  // 음수 방지
        postRepository.save(post);
    }
}
