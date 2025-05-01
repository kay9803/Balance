package com.judebalance.backend.controller;

import com.judebalance.backend.domain.User;
import com.judebalance.backend.response.UserSearchResponse;
import com.judebalance.backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 친구 요청 / 수락 / 조회 컨트롤러
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // ✅ 친구 요청 보내기
    @PostMapping("/request/{toUserId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable Long toUserId,
                                                    Authentication authentication) {
        String fromUsername = authentication.getName();
        friendService.sendFriendRequest(fromUsername, toUserId);
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }

    // ✅ 친구 요청 수락
    @PostMapping("/accept/{fromUserId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long fromUserId,
                                                      Authentication authentication) {
        String toUsername = authentication.getName();
        friendService.acceptFriendRequest(toUsername, fromUserId);
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

    // ✅ 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<UserSearchResponse>> getFriends(Authentication authentication) {
        String username = authentication.getName();
        List<User> friends = friendService.getFriends(username);

        // 간단한 응답 포맷
        List<UserSearchResponse> response = friends.stream()
                .map(user -> new UserSearchResponse(
                        user.getId(),
                        user.getNickname(),
                        user.getAge(),
                        null  // profileImageUrl은 null로 대체 (필요 시 수정)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/cancel/{toUserId}")
public ResponseEntity<String> cancelFriendRequest(@PathVariable Long toUserId,
                                                  Authentication authentication) {
    String fromUsername = authentication.getName();
    friendService.cancelFriendRequest(fromUsername, toUserId);
    return ResponseEntity.ok("친구 요청을 취소했습니다.");
}


@GetMapping("/pending")
public ResponseEntity<List<UserSearchResponse>> getPendingRequests(Authentication authentication) {
    String username = authentication.getName();
    List<User> fromUsers = friendService.getPendingFriendRequests(username);

    List<UserSearchResponse> response = fromUsers.stream()
            .map(user -> new UserSearchResponse(
                    user.getId(),
                    user.getNickname(),
                    user.getAge(),
                    null
            ))
            .collect(Collectors.toList());

    return ResponseEntity.ok(response);
}

}
