package com.judebalance.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.Post;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.PostRepository;
import com.judebalance.backend.response.PostResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;

    public void createPostFromWorkout(User user, WorkoutRecord request) {
        if (!"public".equalsIgnoreCase(request.getVisibility())) return;


        String content = generateAutoContent(request); // 예: 운동 이름, 세트, 시간 요약

        Post post = Post.builder()
                .user(user)
                .workoutRecord(request)
                .content(content)
                .isPublic(true)
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    private String generateAutoContent(WorkoutRecord request) {
            return "오늘 '" + request.getExerciseName() + "' 운동을 완료했어요!"; // 간단 예시
    }

    /**
     * 커뮤니티 포스트 조회 API
     * @return
     */

    public List<PostResponse> getAllPublicPostResponses() {
    return postRepository.findByIsPublicTrueOrderByCreatedAtDesc()
            .stream()
            .map(post -> new PostResponse(
                    post.getUser().getUsername(),
                    post.getWorkoutRecord().getExerciseName(),
                    post.getContent(),
                    post.getCreatedAt()
                        , post.getId() // ID 추가
            ))
            .toList();
}

}
