package com.judebalance.backend.service;

import com.judebalance.backend.domain.Friend;
import com.judebalance.backend.domain.FriendStatus;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.FriendRepository;
import com.judebalance.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    /**
     * 친구 요청 보내기
     */
    @Transactional
    public void sendFriendRequest(String fromUsername, Long toUserId) {
        User fromUser = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new RuntimeException("요청자 정보를 찾을 수 없습니다."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("수신자 정보를 찾을 수 없습니다."));

        if (fromUser.equals(toUser)) {
            throw new RuntimeException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        boolean alreadyExists = friendRepository.findByFromUserAndToUser(fromUser, toUser).isPresent();
        if (alreadyExists) {
            throw new RuntimeException("이미 친구 요청을 보냈거나 수락된 상태입니다.");
        }

        Friend friend = Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendStatus.PENDING)
                .build();

        friendRepository.save(friend);
    }

    /**
     * 친구 요청 수락
     */
    @Transactional
    public void acceptFriendRequest(String toUsername, Long fromUserId) {
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("수신자 정보를 찾을 수 없습니다."));
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("요청자 정보를 찾을 수 없습니다."));

        Friend friend = friendRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new RuntimeException("친구 요청이 존재하지 않습니다."));

        friend.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend);
    }

    /**
     * 친구 목록 조회
     */
    @Transactional(readOnly = true)
    public List<User> getFriends(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Friend> friends = friendRepository.findByStatusAndFromUserOrToUser(FriendStatus.ACCEPTED, user, user);

        return friends.stream()
                .map(f -> f.getFromUser().equals(user) ? f.getToUser() : f.getFromUser())
                .distinct()
                .collect(Collectors.toList());
    }
    @Transactional
public void cancelFriendRequest(String fromUsername, Long toUserId) {
    User fromUser = userRepository.findByUsername(fromUsername)
            .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));
    User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new RuntimeException("대상 사용자를 찾을 수 없습니다."));

    Friend friend = friendRepository.findByFromUserAndToUser(fromUser, toUser)
            .orElseThrow(() -> new RuntimeException("친구 요청이 존재하지 않습니다."));

    if (friend.getStatus() != FriendStatus.PENDING) {
        throw new RuntimeException("이미 수락된 친구 요청은 취소할 수 없습니다.");
    }

    friendRepository.delete(friend);
}

@Transactional(readOnly = true)
public List<User> getPendingFriendRequests(String toUsername) {
    User toUser = userRepository.findByUsername(toUsername)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    return friendRepository.findByToUserAndStatus(toUser, FriendStatus.PENDING)
            .stream()
            .map(Friend::getFromUser)
            .collect(Collectors.toList());
}


}
