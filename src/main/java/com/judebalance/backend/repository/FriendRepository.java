package com.judebalance.backend.repository;

import com.judebalance.backend.domain.Friend;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.domain.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUserAndToUser(User from, User to);
    List<Friend> findByToUserAndStatus(User to, FriendStatus status);
    List<Friend> findByFromUserAndStatus(User from, FriendStatus status);
    List<Friend> findByStatusAndFromUserOrToUser(FriendStatus status, User from, User to);
}
