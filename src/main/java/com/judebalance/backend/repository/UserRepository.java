package com.judebalance.backend.repository;

import com.judebalance.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);     // 이메일 중복 체크
    boolean existsByUsername(String username); // 사용자명 중복 체크
    List<User> findByNicknameContainingIgnoreCase(String keyword);  // 사용자 검색

}
