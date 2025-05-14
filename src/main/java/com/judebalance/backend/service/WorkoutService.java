package com.judebalance.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.WorkoutRecord;
import com.judebalance.backend.repository.WorkoutRecordRepository;
import com.judebalance.backend.request.ExerciseRecordRequest;
import com.judebalance.backend.response.WorkoutRecordResponse;

import lombok.RequiredArgsConstructor;

/**
 * Workout 기록 저장 및 조회 관련 서비스 로직
 */
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRecordRepository workoutRecordRepository;

    /**
     * 운동 기록 저장
     * @param user 현재 로그인한 사용자
     * @param request 운동 기록 요청 DTO
     */
    public void saveWorkoutRecord(User user, ExerciseRecordRequest request) {
        WorkoutRecord record = WorkoutRecord.builder()
            .user(user)
            .exerciseName(request.getExerciseName())
            .duration(request.getTotalTime())
            .completedSets(request.getCompletedSets())
            .intensityScore(request.getIntensityScore())
            .feedback(request.getFeedback())
            .memo(request.getMemo())
            .visibility(request.getVisibility())
            .date(LocalDate.parse(request.getDate()))
            .build();

        workoutRecordRepository.save(record);
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
}
