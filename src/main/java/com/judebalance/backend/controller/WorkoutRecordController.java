// src/main/java/com/judebalance/backend/controller/WorkoutRecordController.java
package com.judebalance.backend.controller;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.request.WorkoutRecordRequest;
import com.judebalance.backend.request.WorkoutRecordUpdateRequest;
import com.judebalance.backend.response.WorkoutRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 운동 기록 저장, 조회, 수정, 삭제, 최근 조회를 담당하는 컨트롤러
 */
@RestController
@RequestMapping("/api/workout/records")
@RequiredArgsConstructor
public class WorkoutRecordController {

    private final WorkoutRecordRepository workoutRecordRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> saveRecord(@RequestBody WorkoutRecordRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        WorkoutRecord record = new WorkoutRecord();
        record.setUser(user);
        record.setExerciseName(request.getExerciseName());
        record.setDuration(request.getDuration());
        record.setDate(LocalDate.now()); // 오늘 날짜 자동 입력

        workoutRecordRepository.save(record);

        return ResponseEntity.ok("Workout record saved successfully");
    }

    @GetMapping
    public ResponseEntity<List<WorkoutRecordResponse>> getMyRecords(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        List<WorkoutRecord> records = workoutRecordRepository.findByUserOrderByDateDesc(user);

        List<WorkoutRecordResponse> responses = records.stream()
                .map(record -> new WorkoutRecordResponse(
                        record.getId(),
                        record.getExerciseName(),
                        record.getDuration(),
                        record.getDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<String> updateRecord(@PathVariable Long recordId,
                                                @RequestBody WorkoutRecordUpdateRequest updateRequest,
                                                Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        WorkoutRecord record = workoutRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("운동 기록을 찾을 수 없습니다."));

        if (!record.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("자신의 운동 기록만 수정할 수 있습니다.");
        }

        if (updateRequest.getExerciseName() != null) {
            record.setExerciseName(updateRequest.getExerciseName());
        }
        if (updateRequest.getDuration() != null) {
            record.setDuration(updateRequest.getDuration());
        }

        workoutRecordRepository.save(record);

        return ResponseEntity.ok("Workout record updated successfully");
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        WorkoutRecord record = workoutRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("운동 기록을 찾을 수 없습니다."));

        if (!record.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("자신의 운동 기록만 삭제할 수 있습니다.");
        }

        workoutRecordRepository.delete(record);

        return ResponseEntity.ok("Workout record deleted successfully");
    }

    @GetMapping("/recent")
    public ResponseEntity<List<WorkoutRecordResponse>> getRecentRecords(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        List<WorkoutRecord> records = workoutRecordRepository.findByUserOrderByDateDesc(user);

        List<WorkoutRecordResponse> recentResponses = records.stream()
                .filter(record -> !record.getDate().isBefore(sevenDaysAgo)) // 최근 7일 이내 데이터만
                .map(record -> new WorkoutRecordResponse(
                        record.getId(),
                        record.getExerciseName(),
                        record.getDuration(),
                        record.getDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recentResponses);
    }
}
