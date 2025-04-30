// response/UserSearchResponse.java
package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchResponse {
    private Long id;
    private String nickname;
    private Integer age;
    private String profileImageUrl;
}
