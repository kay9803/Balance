package com.judebalance.backend.repository;

import com.judebalance.backend.domain.FriendRequest;
import com.judebalance.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findByReceiverAndAcceptedFalse(User receiver); // 받은 요청
    List<FriendRequest> findBySenderAndAcceptedTrue(User sender);     // 내가 보낸 친구
    List<FriendRequest> findByReceiverAndAcceptedTrue(User receiver); // 나를 친구로 추가한 사람
}

