package com.judebalance.backend.response;

import com.judebalance.backend.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendResponseDto {
    private Long id;
    private String username;
    private int age;
    private String gender; // 0: 여성, 1: 남성


    public static FriendResponseDto from(User user) {
        return new FriendResponseDto(
            user.getId(),
            user.getUsername(),
            user.getAge(),
            user.getGender()
        );
    }

   
}
