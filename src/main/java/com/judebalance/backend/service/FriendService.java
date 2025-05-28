package com.judebalance.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.judebalance.backend.domain.FriendRequest;
import com.judebalance.backend.domain.User;
import com.judebalance.backend.repository.FriendRequestRepository;
import com.judebalance.backend.repository.UserRepository;
import com.judebalance.backend.response.FriendResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    // 친구 요청 보내기
    public String sendFriendRequest(String senderUsername, String receiverUsername) {
        User sender = getUser(senderUsername);
        User receiver = getUser(receiverUsername);

        if (sender.equals(receiver)) throw new IllegalArgumentException("자기 자신에게 요청할 수 없습니다.");

        Optional<FriendRequest> existing = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (existing.isPresent()) throw new IllegalStateException("이미 요청을 보냈습니다.");

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setAccepted(false);
        request.setDate(LocalDate.now());

        friendRequestRepository.save(request);
        return "요청 완료";
    }

        // 친구 요청 목록 조회

    public List<FriendResponseDto> getPendingRequests(String username) {
        User user = getUser(username);
        return friendRequestRepository.findByReceiverAndAcceptedFalse(user).stream()
                .map(FriendRequest::getSender)
                .map(FriendResponseDto::from)
                .toList();
    }
    

        // 친구 목록 조회

 public List<FriendResponseDto> getFriends(String username) {
    User user = getUser(username);
    List<FriendRequest> sent = friendRequestRepository.findBySenderAndAcceptedTrue(user);
    List<FriendRequest> received = friendRequestRepository.findByReceiverAndAcceptedTrue(user);

    return Stream.concat(
        sent.stream().map(FriendRequest::getReceiver),
        received.stream().map(FriendRequest::getSender)
    )
    .map(FriendResponseDto::from)
    .toList();
}


    // 수락
    public void acceptRequest(String username, Long friendId) {
        User receiver = getUser(username);
        User sender = getUserById(friendId);

        FriendRequest request = friendRequestRepository
                .findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        request.setAccepted(true);
        friendRequestRepository.save(request);
    }

    // 거절
    public void rejectRequest(String username, Long friendId) {
        User receiver = getUser(username);
        User sender = getUserById(friendId);

        FriendRequest request = friendRequestRepository
                .findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        friendRequestRepository.delete(request);
    }

    // 친구 삭제 (양방향 모두 삭제는 선택사항)
    public void removeFriend(String username, Long friendId) {
        User user = getUser(username);
        User target = getUserById(friendId);

        friendRequestRepository.findBySenderAndReceiver(user, target).ifPresent(friendRequestRepository::delete);
        friendRequestRepository.findBySenderAndReceiver(target, user).ifPresent(friendRequestRepository::delete);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + username));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저 ID 오류: " + id));
    }
}
