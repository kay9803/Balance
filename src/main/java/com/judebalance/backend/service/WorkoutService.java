package com.judebalance.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.request.WorkoutRecordRequest;
import com.judebalance.backend.response.WorkoutRecordResponse;

import lombok.RequiredArgsConstructor;

/**
 * Workout 기록 저장 및 조회 관련 서비스 로직
 */
@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRecordRepository workoutRecordRepository;
    private final PostService postService;
  

    /**
     * 운동 기록 저장      .duration(request.getTotalTime())
     * @param user 현재 로그인한 사용자
     * @param request 운동 기록 요청 DTO
     */
public WorkoutRecord saveWorkoutRecord(User user, WorkoutRecordRequest request) {
    WorkoutRecord record = WorkoutRecord.builder()
        .user(user)
        .exerciseName(request.getExerciseName())
        .visibility(request.getVisibility())
        .duration(request.getTotalTime())  // Duration으로 변환 필요
        .intensityScore(request.getIntensityScore())
        .feedback(request.getFeedback())
        .completedSets(request.getCompletedSets())
        .memo(request.getMemo())
        .date(LocalDate.parse(request.getDate()))  // 문자열인 경우
        .build();

    workoutRecordRepository.save(record);
    return record;
}


    /**
     * 최근 3회 운동 기록 조회
     * @param user 현재 로그인한 사용자
     * @return WorkoutRecordResponse 리스트
     */
    public List<WorkoutRecordResponse> getRecent3Records(User user) {
        List<WorkoutRecord> records = workoutRecordRepository
            .findTop3ByUserOrderByDateDesc(user);

        return records.stream()
            .map(r -> new WorkoutRecordResponse(
                r.getExerciseName(),
                r.getDuration(),
                r.getCompletedSets(),
                r.getFeedback(),
                r.getIntensityScore(),
                r.getDate().toString()
            ))
            .collect(Collectors.toList());
    }


  /**
 * 최근 7일간 운동 기록을 WorkoutRecordResponse 형태로 반환
 * @param user 현재 로그인한 사용자
 * @return WorkoutRecordResponse 리스트
 */
public List<WorkoutRecordResponse> getRecentWeekRecordResponses(User user) {
    LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
    List<WorkoutRecord> records = workoutRecordRepository.findByUserAndDateAfter(user, oneWeekAgo);

    return records.stream()
        .map(r -> new WorkoutRecordResponse(
            r.getExerciseName(),
            r.getDuration(),
            r.getCompletedSets(),
            r.getFeedback(),
            r.getIntensityScore(),
            r.getDate().toString()
        ))
        .collect(Collectors.toList());
}

// WorkoutService.java
public List<WorkoutRecordResponse> getAllRecords(User user) {
    List<WorkoutRecord> records = workoutRecordRepository.findByUserOrderByDateDesc(user);

    return records.stream().map(record -> new WorkoutRecordResponse(
        record.getExerciseName(),
        record.getDuration(),
        record.getCompletedSets(),
        record.getFeedback(),
        record.getIntensityScore(),
        record.getDate().toString()
    )).collect(Collectors.toList());
}



}

