// src/main/java/com/judebalance/backend/controller/WorkoutRecordController.java
package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.request.WorkoutRecordRequest;
import com.judebalance.backend.response.WorkoutRecordResponse;
import com.judebalance.backend.service.PostService;
import com.judebalance.backend.service.WorkoutService;

import lombok.RequiredArgsConstructor;

/**
 * 운동 기록 저장, 조회, 수정, 삭제, 최근 조회를 담당하는 컨트롤러
 */
@RestController
@RequestMapping("/api/workout/records")
@RequiredArgsConstructor
public class WorkoutRecordController {

    private final WorkoutRecordRepository workoutRecordRepository;
    private final WorkoutService workoutService;
    private final PostService postService;
    private final UserRepository userRepository;
  

    @PostMapping("/save")
    public ResponseEntity<?> saveWorkout(@RequestBody WorkoutRecordRequest request,
                                         Authentication authentication) {
    
        String username = (String) authentication.getPrincipal();
        User entityUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));
    
        // 🏋 workout 저장 + 반환
        WorkoutRecord record = workoutService.saveWorkoutRecord(entityUser, request);
    
        // 🌐 공개 범위이면 커뮤니티 포스트 등록
        if ("public".equalsIgnoreCase(record.getVisibility())) {
            postService.createPostFromWorkout(entityUser, record);
        }
    
        return ResponseEntity.ok().build();
    }
    
    /**
     * 최근 3회 운동 기록 조회 API
     */

@GetMapping("/recent3")
public ResponseEntity<List<WorkoutRecordResponse>> getRecentThree(Authentication authentication) {
    String username = (String) authentication.getPrincipal();  // ✅ 변경
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자 없음"));

    List<WorkoutRecordResponse> responses = workoutService.getRecent3Records(currentUser);
    return ResponseEntity.ok(responses);
}


/**
 * 최근 일주일 운동 기록 조회 API
 */
@GetMapping("/workouts/recent-week")
public ResponseEntity<List<WorkoutRecordResponse>> getRecentWeekRecords(Authentication authentication) {
    String username = (String) authentication.getPrincipal();  // ✅ 변경
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자 없음"));

    List<WorkoutRecordResponse> responses = workoutService.getRecentWeekRecordResponses(currentUser);
    return ResponseEntity.ok(responses);
}

// WorkoutRecordController.java
@GetMapping("/all")
public ResponseEntity<List<WorkoutRecordResponse>> getAllRecords(Authentication authentication) {
    String username = (String) authentication.getPrincipal();
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자 없음"));

    List<WorkoutRecordResponse> responses = workoutService.getAllRecords(currentUser);
    return ResponseEntity.ok(responses);
}

 

}
