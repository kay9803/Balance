// response/UserSearchResponse.java
package com.judebalance.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchResponse {
    private Long id;
    private String username;
    private Integer age;
    private String profileImageUrl;
    private String gender;
}
