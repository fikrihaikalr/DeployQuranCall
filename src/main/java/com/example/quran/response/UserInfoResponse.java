package com.example.quran.response;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private Long id;
    private String email;
    private List<String> roles;
    private String token;

    public UserInfoResponse(Long id, String email, List<String> roles, String token) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.token = token;
    }
}
