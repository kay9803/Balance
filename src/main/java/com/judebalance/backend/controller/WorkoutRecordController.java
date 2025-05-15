// src/main/java/com/judebalance/backend/controller/WorkoutRecordController.java
package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.request.ExerciseRecordRequest;
import com.judebalance.backend.response.WorkoutRecordResponse;
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
    private final UserRepository userRepository;
    private final WorkoutService workoutService;

    /**
     * 운동 기록 저장 API
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveWorkout(@RequestBody ExerciseRecordRequest request,
                                         Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User entityUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        workoutService.saveWorkoutRecord(entityUser, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 최근 3회 운동 기록 조회 API
     */
    @GetMapping("/recent3")
    public ResponseEntity<List<WorkoutRecordResponse>> getRecentThree(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<WorkoutRecordResponse> responses = workoutService.getRecent3Records(currentUser);
        return ResponseEntity.ok(responses);
    }
}
