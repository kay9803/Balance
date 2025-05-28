package com.judebalance.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.judebalance.backend.request.FriendIdDto;
import com.judebalance.backend.request.FriendRequestDto;
import com.judebalance.backend.response.FriendResponseDto;
import com.judebalance.backend.service.FriendService;
import com.judebalance.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService; // 현재 사용자 조회용 (ex: SecurityContext에서 username 추출)

    // 1. 친구 목록 조회
   @GetMapping("/list")
public List<FriendResponseDto> getFriendList(@RequestHeader("username") String username) {
    return friendService.getFriends(username);
}

    // 2. 친구 요청 보내기
    @PostMapping("/add")
    public String sendRequest(@RequestHeader("username") String sender,
                              @RequestBody FriendRequestDto request) {
        return friendService.sendFriendRequest(sender, request.getTargetUsername());
    }

    // 3. 받은 친구 요청 목록 조회
   
@GetMapping("/pending")
public List<FriendResponseDto> getPending(@RequestHeader("username") String username) {
    return friendService.getPendingRequests(username);
}

    // 4. 친구 요청 수락
    @PostMapping("/accept")
    public String acceptRequest(@RequestHeader("username") String receiver,
                                 @RequestBody FriendIdDto dto) {
        friendService.acceptRequest(receiver, dto.getFriendId());
        return "수락 완료";
    }

    // 5. 친구 요청 거절
    @PostMapping("/reject")
    public String rejectRequest(@RequestHeader("username") String receiver,
                                 @RequestBody FriendIdDto dto) {
        friendService.rejectRequest(receiver, dto.getFriendId());
        return "거절 완료";
    }

    // 6. 친구 삭제
    @PostMapping("/remove")
    public String removeFriend(@RequestHeader("username") String username,
                               @RequestBody FriendIdDto dto) {
        friendService.removeFriend(username, dto.getFriendId());
        return "삭제 완료";
    }
}
